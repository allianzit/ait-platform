import { AitMenuItem } from './index';

export class AitUser {
  public id: number;
  public enabled: boolean = true;
  public username: string;
  public firstName: string;
  public lastName: string;
  public email: string;
  public roles: string[];
  public menu: AitMenuItem[];

  constructor() {
    this.username = '';
    this.firstName = '';
    this.lastName = '';
    this.email = '';
  }
}
