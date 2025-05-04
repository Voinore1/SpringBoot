import {ReactNode} from "react";

export interface IUserRegisterRequest {
    username: string;
    password: string;
}

export interface IUserLoginRequest {
    username: string;
    password: string;
}

export interface LoginButtonProps{
    title: string
    onLogin: (token: string) => void
    icon: ReactNode
}