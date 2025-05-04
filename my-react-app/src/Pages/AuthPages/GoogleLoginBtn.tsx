import React from "react";
import { useGoogleLogin } from '@react-oauth/google';
import { Button } from 'antd';
import {LoginButtonProps} from "../../models/Auth.ts";

const GoogleLoginButton: React.FC<LoginButtonProps> = ({ onLogin, title, icon }) => {

    const login = useGoogleLogin({
        onSuccess: async (authCodeResponse) => {
            console.log("Login Google Token", authCodeResponse.access_token);
            onLogin(authCodeResponse.access_token);

        },
        onError: (error) => {
            console.error('Login Failed:', error);
        },
    });

    return (
        <Button icon={icon} onClick={()=>login()}>
            {title}
        </Button>
    );
};

export default GoogleLoginButton;