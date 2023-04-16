export interface User {
    email: string;
    jwt: string;
    id: number;
    status: number;
    name: string;
    lastName?: string;
    tokenExpirationDate: number
}

export const STATUS_ACTIVE = 1;
export const STATUS_INACTIVE = 0;

export interface UserData {
    email: string;
    jwt: string;
    id: number;
    status: number;
    expiresIn: number;
    name: string;
    lastName?: string;
}