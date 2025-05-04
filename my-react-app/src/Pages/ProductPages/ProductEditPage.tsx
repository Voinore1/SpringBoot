import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {Form, Input, Select, Upload, UploadFile} from "antd";
import TextArea from "antd/es/input/TextArea";
import {PlusOutlined} from '@ant-design/icons';
import {DragDropContext, Draggable, Droppable, DropResult} from "@hello-pangea/dnd";
import {APP_ENV} from "../../env";
import {useGetAllCategoriesQuery} from "../../services/categoriesAPI.ts";
import {useGetProductByIdQuery, useUpdateProductMutation} from "../../services/productsAPI.ts";
import {IProductEdit} from "../../models/Product.ts";

/* eslint-disable react-hooks/rules-of-hooks */
const EditProductPage: React.FC = () => {

    const { id } = useParams<{ id: string }>();
    if (!id || isNaN(parseInt(id, 10))) {
        return <div>Invalid category ID</div>;
    }
    const categoryId: number = parseInt(id, 10);

    const {data: categories, isLoading: categoriesLoading, error: categoriesError} = useGetAllCategoriesQuery();
    const {data: product, isLoading: productLoading, error: productError} = useGetProductByIdQuery(categoryId);
    const [updateProduct, {isLoading, error}] = useUpdateProductMutation();
    const [selectedFiles, setSelectedFiles] = useState<UploadFile[]>([]);
    const navigate = useNavigate();
    const [form] = Form.useForm<IProductEdit>();

    const categoriesData = categories?.map(item => ({
        label: item.name,
        value: item.id,
    }));

    const onSubmit = async (values: IProductEdit) => {
        try {
            values.imageFiles = selectedFiles.map(x=> x.originFileObj as File);
            values.id = product!.id;

            // Викликаємо мутацію для створення продукту
            await updateProduct(values).unwrap();
            navigate('..'); // Перехід до нового продукту
        } catch (err) {
            console.error('Error creating product:', err);
        }
    }

    const onDragEnd = (result: DropResult) => {
        if (!result.destination) return;
        const reorderedFiles = Array.from(selectedFiles);
        const [movedFile] = reorderedFiles.splice(result.source.index, 1);
        reorderedFiles.splice(result.destination.index, 0, movedFile);
        setSelectedFiles(reorderedFiles);
    };

    const handleImageChange = (info: { fileList: UploadFile[] }) => {
        const newFileList = info.fileList.map((file, index) => ({
            ...file,
            uid: file.uid || Date.now().toString(),
            order: index,
        }));
        setSelectedFiles([...selectedFiles, ...newFileList]);
    };

    useEffect(() => {
        if (product) {
            form.setFieldsValue({...product});
            const files = product?.imageLinks.map(x => ({
                uid: x,
                url: `${APP_ENV.REMOTE_IMAGES_URL}Medium/${x}`,
                originFileObj: new File([new Blob([''])], x, { type: 'old-image' })
            }) as UploadFile);
            setSelectedFiles(files);
        }
    }, [product]);

    if (productLoading) return <p>Loading...</p>;
    if (productError) return <p>Error loading product data.</p>;

    return (
        <div className="max-w-xl mx-auto p-6 bg-white shadow-md rounded-lg">
            <h1 className="text-2xl font-bold text-center mb-6">Edit Product</h1>
            <button onClick={() => navigate(-1)}
                    className="px-4 py-2 bg-blue-500 text-white rounded-lg shadow-md hover:bg-blue-700 mb-4"
            >
                Go Back
            </button>

            <Form form={form} onFinish={onSubmit} layout={"vertical"}>
                <Form.Item
                    label="Назва"
                    name="name"
                    htmlFor="name"
                    rules={[
                        {required: true, message: "It is a required field!"},
                        {min: 3, message: "Name must have at least 3 symbols!"},
                    ]}
                >
                    <Input autoComplete="name" className={"w-full p-2 border border-gray-300 rounded mt-2"} />
                </Form.Item>

                {categoriesLoading ? (
                    <p>Loading categories...</p>
                ) : categoriesError ? (
                    <p className="text-red-500">Failed to load categories</p>
                ) : (
                    <Form.Item
                        label="Категорія"
                        name="categoryId"
                        htmlFor="categoryId"
                        rules={[{required: true, message: "It is a required field!"}]}
                    >
                        <Select placeholder="Оберіть категорію" options={categoriesData}/>
                    </Form.Item>
                )}

                <Form.Item
                    label="Ціна"
                    name="price"
                    htmlFor="price"
                    rules={[
                        {required: true, message: "It is a required field!"},
                    ]}
                >
                    <Input type="number" autoComplete="price" className={"w-full p-2 border border-gray-300 rounded mt-2"} />
                </Form.Item>

                <Form.Item
                    label="Кількість"
                    name="amount"
                    htmlFor="amount"
                    rules={[
                        {required: true, message: "It is a required field!"},
                    ]}
                >
                    <Input type="number" autoComplete="amount" className={"w-full p-2 border border-gray-300 rounded mt-2"} />
                </Form.Item>

                <Form.Item
                    label="Опис"
                    name="description"
                    htmlFor="description"
                    rules={[
                        {required: true, message: "It is a required field!"},
                    ]}
                >
                    <TextArea rows={4} placeholder="Введіть текст..." maxLength={200} allowClear />
                </Form.Item>

                <DragDropContext onDragEnd={onDragEnd}>
                    <Droppable droppableId="upload-list" direction="horizontal">
                        {(provided) => (
                            <div ref={provided.innerRef} {...provided.droppableProps} className="flex flex-wrap gap-2">
                                {selectedFiles.map((file, index) => (
                                    <Draggable key={file.uid} draggableId={file.uid} index={index}>
                                        {(provided) => (
                                            <div
                                                ref={provided.innerRef}
                                                {...provided.draggableProps}
                                                {...provided.dragHandleProps}
                                            >
                                                <Upload
                                                    listType="picture-card"
                                                    fileList={[file]}
                                                    onRemove={() => {
                                                        const newFileList = selectedFiles.filter(f => f.uid !== file.uid);
                                                        setSelectedFiles(newFileList);
                                                    }}
                                                />
                                            </div>
                                        )}
                                    </Draggable>
                                ))}
                                {provided.placeholder}
                            </div>
                        )}
                    </Droppable>
                </DragDropContext>

                <Upload
                    multiple
                    listType="picture-card"
                    beforeUpload={() => false}
                    onChange={handleImageChange}
                    fileList={[]}
                    accept="image/*"
                >
                    <div>
                        <PlusOutlined/>
                        <div style={{marginTop: 8}}>Додати</div>
                    </div>
                </Upload>

                <div className="flex justify-center">
                    <button
                        type="submit"
                        disabled={isLoading}
                        className="bg-blue-500 text-white p-2 rounded w-full md:w-1/2 mt-4"
                    >
                        {isLoading ? 'Saving...' : 'Update Product'}
                    </button>
                </div>
                {error && <p className="text-red-500 mt-2">Error updating product!</p>}
            </Form>
        </div>
    );
};

export default EditProductPage;