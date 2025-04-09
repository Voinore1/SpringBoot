import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import HomePage from './Pages/HomePage';
import CategoriesPage from './Pages/CategoriesPage';
import CreateCategoryPage from "./Pages/CreateCategoryPage.tsx";

const App: React.FC = () => (
    <Router>
        <Routes>
            <Route path="/" element={<Layout />}>
                <Route index element={<HomePage />} />
                <Route path="categories">
                    <Route index element={ <CategoriesPage/> }/>
                    <Route path="create" element={<CreateCategoryPage />} />
                    <Route path="edit:id" element={<CreateCategoryPage />} />
                </Route>

            </Route>
        </Routes>
    </Router>
);

export default App;