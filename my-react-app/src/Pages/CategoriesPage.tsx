import React, {useState} from 'react';
import {Table, Modal, Button} from 'flowbite-react';
import {APP_ENV} from "../env";
import {Link} from "react-router-dom";
import {useDeleteCategoryMutation, useGetAllCategoriesQuery} from "../services/categoriesAPI.ts";
import { LiaEdit } from "react-icons/lia";
import { FaRegTrashAlt } from "react-icons/fa";
import { HiOutlineExclamationCircle } from "react-icons/hi";

const CategoriesPage: React.FC = () => {
    const {data: categories, error, isLoading} = useGetAllCategoriesQuery();
    const [deleteCategory, {isLoading: isDeleting}] = useDeleteCategoryMutation();
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [categoryToDelete, setCategoryToDelete] = useState<number | null>(null);

    const handleDeleteClick = (id: number) => {
        setCategoryToDelete(id);
        setIsModalOpen(true);
    };

    const handleDelete = async () => {
        if (categoryToDelete) {
            try {
                await deleteCategory(categoryToDelete).unwrap();
            } catch (err) {
                console.error('Error deleting category:', err);
            }
        }
        closeDeleteModal();
    };

    const closeDeleteModal = () => {
        setIsModalOpen(false);
        setCategoryToDelete(null);
    };

    if (isLoading) return <p>Loading...</p>;
    if (error) return <p>Error occurred while fetching categories.</p>;

    return (
        <>
            <h1 className="text-4xl text-center font-bold text-blue-700 shadow-lg p-6 bg-opacity-50 rounded-lg">
                Категорії
            </h1>

            {/* Кнопка для переходу на сторінку створення категорії */}
            <div className="flex justify-start mb-6">
                <Link
                    to="create" // Вказуємо маршрут для створення категорії
                    className="px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg shadow-md hover:bg-blue-700"
                >
                    Створити категорію
                </Link>
            </div>

            <div className="overflow-x-auto">
                <Table>
                    <Table.Head>
                        <Table.HeadCell>Назва</Table.HeadCell>
                        <Table.HeadCell>Фото</Table.HeadCell>
                        <Table.HeadCell>Опис</Table.HeadCell>
                        <Table.HeadCell>Дії</Table.HeadCell>
                    </Table.Head>

                    <Table.Body className="divide-y">
                        {categories?.map((category) => (
                            <Table.Row key={category.id} className="bg-white dark:border-gray-700 dark:bg-gray-800">
                                <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">
                                    {category.name}
                                </Table.Cell>
                                <Table.Cell>
                                    <img
                                        src={`${APP_ENV.REMOTE_IMAGES_URL}/Medium/${category.image}`}
                                        alt={category.name}
                                        className="w-16 h-16 object-cover rounded"
                                    />
                                </Table.Cell>
                                <Table.Cell>{category.description}</Table.Cell>
                                <Table.Cell>
                                    <div className="flex">
                                        <Link to={`edit/${category.id}`}>
                                            <LiaEdit className="mx-1 h-6 w-6 text-gray-700"/>
                                        </Link>
                                        <a>
                                            <FaRegTrashAlt onClick={() => handleDeleteClick(category.id)} className="mx-1 h-6 w-6 text-red-800" />
                                        </a>
                                    </div>
                                </Table.Cell>
                            </Table.Row>
                        ))}
                    </Table.Body>
                </Table>
            </div>

            <Modal dismissible show={isModalOpen} size="md" onClose={() => closeDeleteModal()} popup>
                <Modal.Header />
                <Modal.Body>
                    <div className="text-center">
                        <HiOutlineExclamationCircle className="mx-auto mb-4 h-14 w-14 text-gray-400 dark:text-gray-200" />
                        <h3 className="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">
                            Are you sure you want to delete this category?
                        </h3>
                        <div className="flex justify-center gap-4">
                            <Button color="failure" onClick={() => handleDelete()} disabled={isDeleting}>
                                {isDeleting ? "Deleting..." : "Yes, I'm sure"}
                            </Button>
                            <Button color="gray" onClick={() => closeDeleteModal()}>
                                No, cancel
                            </Button>
                        </div>
                    </div>
                </Modal.Body>
            </Modal>
        </>
    );
};

export default CategoriesPage;