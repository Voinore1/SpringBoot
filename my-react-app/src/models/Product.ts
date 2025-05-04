export interface Product {
    id: number;
    name: string;
    description: string;
    creationTime: string;
    amount: number;
    price: number;
    categoryId: number;
    categoryName: string;
    imageLinks: string[];
}

export interface IProductCreate {
    amount: number;
    categoryId: number;
    description: string;
    imageFiles: File[] | null;
    name: string;
    price: number;
}

export interface IProductEdit extends IProductCreate {
    id: number;
}