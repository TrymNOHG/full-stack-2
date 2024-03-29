import axios from "axios"
import sessionToken from "@/features/SessionToken.js";

const BASE_URL = "http://localhost:5173/api/user"

export const loginUser = async (userDTO) => {
    return axios.post(`${BASE_URL}/login`, userDTO);
}

export const registerUser = async (userDTO) => {
    return axios.post(`${BASE_URL}/register`, userDTO);
}

export const getUser = async () => {
    return axios.get(`${BASE_URL}/user/get/info`, {
        headers: {
            Authorization: `Bearer ${await sessionToken()}`,
        },
    })
}

export const checkSuperUser = async (quizId) => {
    return axios.get(`${BASE_URL}/superuser`, {
        data : { quizId },
        headers: {
            Authorization: `Bearer ${await sessionToken()}`,
        },
    })
}

export const getSearchedQuizzes = async (searchword) => {
    return axios.get(`${BASE_URL}/quiz/searchQuiz`, {
        data: { searchword },
    })
}

export const getMoreQuizzes = async (diff) => {
    return axios.get(`${BASE_URL}/quiz/moreQuizzes`, {
        data: { diff }
    })
}

export const getQuizzesByDifficulty = async (diff) => {
    return axios.get(`${BASE_URL}/quiz/QuizzDiff`, {
        data: { diff }
    })
}