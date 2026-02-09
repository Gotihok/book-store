import isbns from "./data/isbn-list.json";
import books from "./data/books.json";

export interface Book {
    title: string;
    author: string;
    publisher: string;
    isbn: string;
}

export function getBooksWithIsbn(): Book[] {
    console.log("Isbns number: ", isbns.length);

    // Assign ISBNs
    books.forEach((book, index) => {
        if (index < isbns.length) {
            book.isbn = isbns[index].replace(/-/g, "");
        } else {
            console.warn(`No ISBN available for book: ${book.title}`);
        }
    });
    console.log(`Assigned ISBNs to ${books.length} books`);

    return books;
}