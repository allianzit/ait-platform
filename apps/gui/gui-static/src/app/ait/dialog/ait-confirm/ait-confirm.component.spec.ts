import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AitConfirmComponent } from './ait-confirm.component';

describe('AitConfirmComponent', () => {
  let component: AitConfirmComponent;
  let fixture: ComponentFixture<AitConfirmComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AitConfirmComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AitConfirmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
