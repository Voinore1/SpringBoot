import { configureStore } from '@reduxjs/toolkit';
import { categoriesApi } from '../services/categoriesAPI';
import {productsApi} from "../services/productsAPI.ts";
import {authApi} from "../services/authAPI.ts";

export const store = configureStore({
    reducer: {
        [categoriesApi.reducerPath]: categoriesApi.reducer,
        [productsApi.reducerPath]: productsApi.reducer,
        [authApi.reducerPath]: authApi.reducer,
    },
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware().concat(
            categoriesApi.middleware,
            productsApi.middleware,
            authApi.middleware,
            ),
    });

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
