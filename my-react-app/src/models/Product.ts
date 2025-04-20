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