import { MenuItem } from '../../home/menu/shared/menu-item';

export class AitUser {
    public id: number;
    public username: string;
    public firstName: string;
    public lastName: string;
    public email: string;
    public roles: string[];
    public menu: MenuItem[];
    constructor() { }
}
