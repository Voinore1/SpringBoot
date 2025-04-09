import React from "react";
import { Table } from "flowbite-react";
import { Category } from "../models/Category";


const CategoryRow: React.FC<{ category: Category }> = ({ category }) => {
  return (
    <Table.Row key={category.id} className="bg-white dark:border-gray-700 dark:bg-gray-800">
      <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">
        {category.name}
      </Table.Cell>
      <Table.Cell>
        <img src={category.image} alt={category.image} className="w-16 h-16 object-cover" />
      </Table.Cell>
      <Table.Cell>{category.description}</Table.Cell>
      <Table.Cell>
        <a
          href="#"
          className="font-medium text-cyan-600 hover:underline dark:text-cyan-500"
        >
          Змінити
        </a>
      </Table.Cell>
    </Table.Row>
  );
};

export default CategoryRow;
