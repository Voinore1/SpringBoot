import './App.css';
import { Button } from "flowbite-react";
import { APP_ENV } from "./env";
import { Table } from "flowbite-react";
import CategoryRow from './components/categoryRow';
import { useGetAllCategoriesQuery } from './services/categoriesAPI';

function App() {
  console.log("App started", APP_ENV.REMOTE_BASE_URL);

  const CategoriesTable: React.FC = () => {

    const { data: categories, error, isLoading } = useGetAllCategoriesQuery();

    if (isLoading) return <p>Loading...</p>;
    if (error) return <p>Error occurred while fetching categories.</p>;

    return (
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
              <CategoryRow key={category.id} category={category} />
            ))}
          </Table.Body>
        </Table>
      </div>
    );
  };

  return (
    <>
      <h1 className="text-3xl font-bold underline">Hello world!</h1>
      <Button color="purple">Purple</Button>
      <CategoriesTable />
    </>
  );
}

export default App;
