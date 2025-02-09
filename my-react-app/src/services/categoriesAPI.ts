import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import {APP_ENV} from "../env";
import { Category } from '../models/Category';

export const categoriesApi = createApi({
    reducerPath: 'categoriesApi',
    baseQuery: fetchBaseQuery({ baseUrl: `${APP_ENV.REMOTE_BASE_URL}/api/` }),
    endpoints: (builder) => ({
        getAllCategories: builder.query<Category[], void>({
            query: () => 'categories',
        }),
    }),
});

export const { useGetAllCategoriesQuery } = categoriesApi;
