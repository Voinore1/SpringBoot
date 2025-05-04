import React from "react";
import {Button, Form, Input} from "antd";
import {GoogleOAuthProvider} from "@react-oauth/google";
import { GoogleOutlined } from '@ant-design/icons';
import {IUserLoginRequest} from "../../models/Auth.ts";
import {useNavigate} from "react-router-dom";
import {useLoginUserMutation} from "../../services/authAPI.ts";
import GoogleLoginBtn from "./GoogleLoginBtn.tsx";

const {Item} = Form;

const LoginPage: React.FC = () => {

    const [form] = Form.useForm<IUserLoginRequest>();
    const navigate = useNavigate();
    const [loginUser] = useLoginUserMutation();

    const onFinish = async (values: IUserLoginRequest) => {

        console.log("Login user", values);
        try {
            console.log("Login user", values);
            const response = await loginUser(values).unwrap();
            console.log("Логін успішний, токен: ", response);
            navigate("..");
        } catch (error) {
            console.error("Помилка при вході", error);
        }
    }

    const onLoginGoogleResult = (tokenGoogle: string) => {
        console.log("google token", tokenGoogle);
    }

    return (
        <>
            <GoogleOAuthProvider clientId={"688315354046-isd3q5qkjaj88uaj9oudrldsf18bm592.apps.googleusercontent.com"}>
                <h1 className={"text-center text-4xl font-bold text-blue-500"}>Вхід на сайт</h1>

                <div style={{maxWidth: '400px', margin: '0 auto'}}>
                    <Form
                        form={form}
                        onFinish={onFinish}
                        layout="vertical">

                        <Item
                            name="username"
                            label={"Електронна пошта"}
                            rules={[
                                {required: true, message: "Вкажіть свою пошшту"},
                                {type: "email", message: "Введіть коректний email"}
                            ]}>
                            <Input placeholder={"Електронна пошта"}/>
                        </Item>

                        <Item
                            name="password"
                            label="Пароль"
                            rules={[
                                {required: true, message: "Введіть пароль"},
                                {min: 6, message: "Пароль має містити щонайменше 6 символів"}
                            ]}
                        >
                            <Input.Password placeholder="Введіть пароль"/>
                        </Item>


                        <Item>
                            <Button type="primary" htmlType="submit">
                                Вхід
                            </Button>
                        </Item>

                        <GoogleLoginBtn icon={<GoogleOutlined />} title='Увійти з Google' onLogin={onLoginGoogleResult} />

                    </Form>
                </div>
            </GoogleOAuthProvider>
        </>
    )
}

export default LoginPage;