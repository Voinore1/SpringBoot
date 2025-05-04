import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {APP_ENV} from "../env";
import {IProductCreate, IProductEdit, Product} from "../models/Product.ts";
import {serialize} from "object-to-formdata";

export const productsApi = createApi({
    reducerPath: 'productsApi',
    baseQuery: fetchBaseQuery({ baseUrl: `${APP_ENV.REMOTE_BASE_URL}/api/` }),
    tagTypes: ['Product'],
    endpoints: (builder) => ({
        getAllProducts: builder.query<Product[], void>({
            query: () => 'products',
            providesTags: (result) =>
                result
                    ? [
                        ...result.map(({ id }) => ({ type: 'Product' as const, id })),
                        { type: 'Product' as const, id: 'LIST' },
                    ]
                    : [{ type: 'Product' as const, id: 'LIST' }],
        }),
        getProductById: builder.query<Product, number>({
            query: (id) => `products/${id}`,
            providesTags: (__, _, id) => [{ type: 'Product', id }],
        }),
        createProduct: builder.mutation<Product, IProductCreate>({
            query: (model) => {
                const formData = serialize(model);
                return {
                    url: 'products',
                    method: 'POST',
                    body: formData
                };
            },
            invalidatesTags: [{ type: 'Product', id: 'LIST' }],
        }),
        deleteProduct: builder.mutation({
            query: (id) => ({
                url: `/products/${id}`,
                method: 'DELETE',
            }),
            invalidatesTags: [{ type: 'Product', id: 'LIST' }],
        }),
        updateProduct: builder.mutation<void, IProductEdit>({
            query: ({ id, ...model }) => {
                try {
                    const formData = serialize(model);
                    return {
                        url: `products/${id}`,
                        method: 'PUT',
                        body: formData
                    };
                } catch {
                    throw new Error("Error serializing the form data.");
                }
            },
            invalidatesTags: (_, __, { id }) => [
                { type: 'Product', id },
                { type: 'Product', id: 'LIST' },
            ],
        }),
    }),
});

export const { useGetAllProductsQuery, useUpdateProductMutation, useGetProductByIdQuery, useCreateProductMutation, useDeleteProductMutation} = productsApi;
