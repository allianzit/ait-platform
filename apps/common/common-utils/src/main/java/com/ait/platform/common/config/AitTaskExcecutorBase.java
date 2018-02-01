/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ait.platform.common.config;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;

import com.ait.platform.common.exception.AitException;
import com.ait.platform.common.logger.AitLogger;
import com.ait.platform.common.model.entity.AitTask;
import com.ait.platform.common.model.vo.AitTaskVO;
import com.ait.platform.common.service.IAitTaskSrv;
import com.ait.platform.common.util.AitLocalhostIpAddress;

/**
 * Tasks executor
 * 
 * @author AllianzIT
 *
 */
public abstract class AitTaskExcecutorBase implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(AitTaskExcecutorBase.class);

	private static int serverPort;

	private static String serverIp;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private IAitTaskSrv taskSrv;

	@Override
	public void onApplicationEvent(final EmbeddedServletContainerInitializedEvent event) {
		try {
			serverPort = event.getEmbeddedServletContainer().getPort();
			serverIp = AitLocalhostIpAddress.search().getHostAddress();
			// se actualizan las tareas que han quedado en ejecucion (cuando se cierra el servidor mientras se estaba ejecutando la tarea)
			taskSrv.updaTasks(serverIp, serverPort);
		} catch (final UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Scheduled(cron = "0/60 * * * * *")
	protected void runTasks() {
		AitLogger.debug(logger, "Consultando las tareas a ejecutar en el servidor {}:{}", serverIp, serverPort);

		// FIXME contemplar solo tareas que no se esten ejecutando actualmente????
		// se consulta la lista de tareas que se pueden ejecutar en el servidor
		final Collection<AitTask> tasks = taskSrv.getTasks(serverIp, serverPort);

		AitLogger.info(logger, "Tareas programadas a ejecutar: {}", tasks.size());
		for (final AitTask task : tasks) {
			if (canRun(task)) {
				// se marca la tarea para que no sea tenida en cuenta hasta no terminar su ejecucion actual
				task.setIsRunning(true);
				taskSrv.updateTaskState(task);

				// se ejecuta la tarea en un hilo aparte para impedir el bloqueo de las demas tareas
				executetask(task);
			}
		}
	}

	private boolean canRun(AitTask task) {
		final long now = new Date().getTime();
		// minima hora de ejecucion
		Calendar min = Calendar.getInstance();
		min.set(Calendar.HOUR_OF_DAY, task.getMinHour());
		min.set(Calendar.MINUTE, 0);

		// maxima hora de ejecucion
		Calendar max = Calendar.getInstance();
		max.set(Calendar.HOUR_OF_DAY, task.getMaxHour());
		max.set(Calendar.MINUTE, 0);

		return now > min.getTimeInMillis() && now < max.getTimeInMillis();
	}

	private void executetask(final AitTask task) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final Date now = new Date();
				try {
					AitLogger.debug(logger, "Procesando tarea: {}", task.getBean());
					// se ejecuta la tarea si la anterior ejecucion fue exitosa o si no se debe parar la ejecucion cuando hay errores
					if (!task.getStopOnFail() || task.getSuccessful()) {
						// se instancia la tarea
						final AitTaskBase bean = context.getBean(task.getBean(), AitTaskBase.class);

						// si se pudo instancia, se asigna la ultima fecha de ejecución
						task.setLastExecDate(now);

						// se ejcuta la tarea y se recibe en indicador de exito
						final AitTaskVO vo = new AitTaskVO(task.getServerIp(), task.getServerPort(), task.getMaxTries(), task.getLastExecDate(), task.getSuccessful(), task.getLastSuccessfulDate(), task.getStopOnFail());

						// ejecuta la tarea y se espera el estado de ejecucion de la misma
						final boolean successful = bean.runTask(vo);

						// se asigna el indicador de exito de la tarea
						task.setSuccessful(successful);

						// si la tarea fue ejecutada exitosamente, se asigna la ultima fecha
						if (successful) {
							task.setLastSuccessfulDate(now);
						}

						// se guarda el mensaje de estado de la ultima ejecucion
						task.setLastExecMsg(bean.getResultMsg());
					}
				} catch (final BeanNotOfRequiredTypeException e) {
					AitLogger.error(logger, "La tarea programada no extiende a AitTaskBase. Verifique que la tarea ({}) extiende a com.ait.platform.common.config.AitTaskBase", task.getBean());
					throw new AitException(e, "La tarea programada no extiende a AitTaskBase.", "Verifique que la tarea (" + task.getBean() + ") extiende a com.ait.platform.common.AitTaskBase");
				} catch (final AitException e) {
					task.setSuccessful(false);
					task.setLastExecMsg(e.getCompleteMessage());
				} catch (Exception e) {
					e.printStackTrace();
					task.setSuccessful(false);
					if (e.getMessage() != null) {
						task.setLastExecMsg(e.getMessage());
					} else {
						task.setLastExecMsg("Excepcion no captura al ejecutar la tarea" + e.toString());
					}

				} finally {
					// se actualiza el siguiente tiempo de ejecucion
					final Date nextExecDate = new Date(now.getTime() + task.getIntervalMinutes() * 60000);
					task.setNextExecDate(nextExecDate);

					// se cambia la bandera que indica que la tarea no se esta ejecutando
					task.setIsRunning(false);

					// se actualiza el estado de la tarea
					taskSrv.updateTaskState(task);
				}
			}
		}).start();

	}

}