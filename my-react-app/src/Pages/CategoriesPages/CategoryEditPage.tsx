/* eslint-disable react-hooks/rules-of-hooks */
import React, { useEffect, useState } from 'react';
import { useGetCategoryByIdQuery, useUpdateCategoryMutation } from '../../services/categoriesAPI.ts';
import { useNavigate, useParams } from 'react-router-dom';
import {APP_ENV} from "../../env";

const EditCategoryPage: React.FC = () => {
    // Extract category ID from URL parameters
    const { id } = useParams<{ id: string }>();
    if (!id || isNaN(parseInt(id, 10))) {
        return <div>Invalid category ID</div>;
    }
    const categoryId: number = parseInt(id, 10);

    // Fetch category data and set up mutation
    const { data: category, isLoading, error } = useGetCategoryByIdQuery(categoryId);
    const [updateCategory, { isLoading: isUpdating }] = useUpdateCategoryMutation();
    const navigate = useNavigate();

    // State for form fields and image handling
    const [name, setName] = useState<string>('');
    const [description, setDescription] = useState<string>('');
    const [imageFile, setImageFile] = useState<File | null>(null);
    const [previewUrl, setPreviewUrl] = useState<string | null>(null);
    const [errorMessage, setErrorMessage] = useState<string>('');

    // Populate form fields when category data is fetched
    useEffect(() => {
        if (category) {
            setName(category.name);
            setDescription(category.description);
        }
    }, [category]);

    // Generate preview URL for new image and clean up
    useEffect(() => {
        if (imageFile) {
            const url = URL.createObjectURL(imageFile);
            setPreviewUrl(url);
            // Cleanup function to revoke URL and prevent memory leaks
            return () => URL.revokeObjectURL(url);
        } else {
            setPreviewUrl(null);
        }
    }, [imageFile]);

    // Handle form submission
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setErrorMessage('');

        const formData = new FormData();
        formData.append('name', name);
        formData.append('description', description);
        if (imageFile) {
            formData.append('imageFile', imageFile); // Include new image if selected
        }

        try {
            await updateCategory({ id: categoryId, formData }).unwrap();
            navigate('..'); // Redirect after successful update
        } catch (err) {
            setErrorMessage('Failed to update category');
            console.error('Update error:', err);
        }
    };

    // Loading and error states
    if (isLoading) return <div>Loading...</div>;

    return (
        <div className="max-w-2xl mx-auto mt-10 p-6 bg-white rounded-2xl shadow-lg">
            <h1 className="text-2xl font-semibold mb-6">Edit Category</h1>

            {error && (
                <div className="mb-4 p-3 bg-red-100 text-red-600 rounded-md">
                    Error loading category
                </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-6">
                <div>
                    <label className="block mb-1 text-gray-700">Name</label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    />
                </div>

                <div>
                    <label className="block mb-1 text-gray-700">Description</label>
                    <textarea
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        rows={4}
                    />
                </div>

                <div>
                    <label className="block mb-1 text-gray-700">Image</label>
                    <input
                        type="file"
                        accept="image/*"
                        onChange={(e) => {
                            if (e.target.files && e.target.files.length > 0) {
                                setImageFile(e.target.files[0]);
                            }
                        }}
                        className="w-full"
                    />

                    {/* Show selected image preview OR existing image */}
                    {previewUrl ? (
                        <img
                            src={previewUrl}
                            alt="New Preview"
                            className="mt-4 h-48 object-cover rounded-lg shadow-md"
                        />
                    ) : category?.image ? (
                        <img
                            src={`${APP_ENV.REMOTE_IMAGES_URL}Large/${category.image}`}
                            alt="Current"
                            className="mt-4 h-48 object-cover rounded-lg shadow-md"
                        />
                    ) : (
                        <p className="mt-4 text-sm text-gray-500 italic">No image available</p>
                    )}
                </div>


                {errorMessage && (
                    <div className="text-red-600 font-medium">{errorMessage}</div>
                )}

                <button
                    type="submit"
                    disabled={isUpdating}
                    className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
                >
                    {isUpdating ? 'Updating...' : 'Update Category'}
                </button>
            </form>
        </div>
    );
};

export default EditCategoryPage;