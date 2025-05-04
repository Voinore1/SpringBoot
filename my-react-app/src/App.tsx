import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import HomePage from './Pages/HomePage';
import CategoriesPage from './Pages/CategoriesPages/CategoriesPage.tsx';
import CreateCategoryPage from "./Pages/CategoriesPages/CreateCategoryPage.tsx";
import EditCategoryPage from "./Pages/CategoriesPages/CategoryEditPage.tsx";
import ProductsPage from "./Pages/ProductPages/ProductsPage.tsx";
import CreateProductPage from "./Pages/ProductPages/CreateProductPage.tsx";
import EditProductPage from "./Pages/ProductPages/ProductEditPage.tsx";
import RegisterPage from "./Pages/AuthPages/RegisterPage.tsx";
import LoginPage from "./Pages/AuthPages/LoginPage.tsx";

const App: React.FC = () => (
    <Router>
        <Routes>
            <Route path="/" element={<Layout />}>
                <Route index element={<HomePage />} />
                <Route path="categories/">
                    <Route index element={ <CategoriesPage/> }/>
                    <Route path="create" element={<CreateCategoryPage />} />
                    <Route path="edit/:id" element={<EditCategoryPage />} />
                </Route>
                <Route path="products/">
                    <Route index element={ <ProductsPage/> }/>
                    <Route path="create" element={ <CreateProductPage/> }/>
                    <Route path="edit/:id" element={<EditProductPage />} />
                </Route>
                <Route path="register" element={<RegisterPage/>}/>
                <Route path="login" element={<LoginPage/>}/>
            </Route>
        </Routes>
    </Router>
);

export default App;