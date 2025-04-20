import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {APP_ENV} from "../env";
import {Product} from "../models/Product.ts";

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
    }),
});

export const { useGetAllProductsQuery} = productsApi;
