import { MenuItem } from '../menu/shared/menu-item';

export class UserInfo {
  constructor(
    public id: number,
    public username: string,
    public firstName: string,
    public lastName: string,
    public email: string,
    public menu: MenuItem[],
  ) {}

}
