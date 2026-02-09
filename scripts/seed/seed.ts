import {api, setAuthToken} from "./http";
import * as http from "node:http";
import axios from "axios";
import {Book, getBooksWithIsbn} from "./assign-isbn";

async function loginOrRegister() {
    const credentials = {
        username: "_RESERVED_USER_",
        email: "RESERVED@test.com",
        password: "admin"
    };

    try {
        console.log("Trying to log in test user...");
        const loginRes = await api.post("/auth/login", {
            username: credentials.username,
            password: credentials.password
        });
        console.log("Login successful!");
        setAuthToken(loginRes.data.jwtToken);
    } catch (loginError) {
        console.log("Login failed, attempting to register...");

        try {
            const registerRes = await api.post("/auth/register", credentials);
            console.log("Registration successful, logging in...");
            setAuthToken(registerRes.data.jwtToken);
        } catch (error: unknown) {
            if (axios.isAxiosError(error)) {
                console.error("Registration failed:", error.response?.data || error.message);
            } else if (error instanceof Error) {
                console.error("Registration failed:", error.message);
            } else {
                console.error("Registration failed:", error);
            }
        }
    }
}

async function seedProducts() {
    const books: Book[] = getBooksWithIsbn();

    console.log("Seeding books...");

    for (const book of books) {
        await api.post("/books/new", book);
        console.log(`Created book: ${book.title}`);
    }
}

async function waitForPort(host: string, port: number, retries = 100, delay = 1000) {
    for (let i = 0; i < retries; i++) {
        try {
            await new Promise<void>((resolve, reject) => {
                const req = http.get({ host, port, path: "/" }, res => resolve());
                req.on("error", reject);
            });
            console.log(`Backend is up on ${host}:${port}`);
            return;
        } catch {
            console.log(`Waiting for backend... (${i + 1})`);
            await new Promise(r => setTimeout(r, delay));
        }
    }
    throw new Error(`Backend did not start on ${host}:${port}`);
}

async function run() {
    console.log("Starting seed process...");
    await waitForPort("localhost", 8080);
    await loginOrRegister();
    await seedProducts();
    console.log("Demo data seeded successfully");
    process.exit(0);
}

run().catch(err => {
    console.error("!!! Seed failed !!!");
    console.error(err.message ?? err);
    process.exit(1);
});