import { createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react';
import {APP_ENV} from "../env";
import { Category } from '../models/Category';

export const categoriesApi = createApi({
    reducerPath: 'categoriesApi',
    baseQuery: fetchBaseQuery({ baseUrl: `${APP_ENV.REMOTE_BASE_URL}/api/` }),
    tagTypes: ['Category'],
    endpoints: (builder) => ({
        getAllCategories: builder.query<Category[], void>({
            query: () => 'categories',
            providesTags: (result) =>
                result
                    ? [
                        ...result.map(({ id }) => ({ type: 'Category' as const, id })),
                        { type: 'Category' as const, id: 'LIST' },
                    ]
                    : [{ type: 'Category' as const, id: 'LIST' }],
        }),
        getCategoryById: builder.query<Category, number>({
            query: (id) => `categories/${id}`,
            providesTags: (__, _, id) => [{ type: 'Category', id }],
        }),
        createCategory: builder.mutation<Category, FormData>({
            query: (formData) => ({
                url: 'categories',
                method: 'POST',
                body: formData,
            }),
            invalidatesTags: [{ type: 'Category', id: 'LIST' }],
        }),
        deleteCategory: builder.mutation({
            query: (id) => ({
                url: `/categories/${id}`,
                method: 'DELETE',
            }),
            invalidatesTags: [{ type: 'Category', id: 'LIST' }],
        }),
        updateCategory: builder.mutation<Category, { id: number, formData: FormData }>({
            query: ({id, formData})=> ({
                url: `categories/${id}`,
                method: 'PUT',
                body: formData
            }),
            invalidatesTags: (_, __, { id }) => [
                { type: 'Category', id },
                { type: 'Category', id: 'LIST' },
            ],
        }),
    }),
});

export const { useGetAllCategoriesQuery, useCreateCategoryMutation, useDeleteCategoryMutation, useUpdateCategoryMutation, useGetCategoryByIdQuery } = categoriesApi;
