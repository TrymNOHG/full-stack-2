import {defineStore} from 'pinia'
import {fetchUserByUsername, getUser} from "@/services/UserService.js";
import {getPictureFromID, saveFile} from "@/services/ImageService.js";

import {
  addCollaborator,
  addKeyword,
  addQuestion,
  createQuiz,
  deleteQuestionById,
  deleteQuizById,
  fetchAllQuizzesByUser,
  fetchCategories,
  fetchFilteredQuizzes,
  patchQuestion,
  removeCollaborator,
  updateQuiz
} from "@/services/QuizService.js"

import {getAllCategories} from "@/services/CategoryService";

export const useUserStore = defineStore('storeUser', {

  state: () => {
    return{
      sessionToken: null,
      sessionTokenExpires: null,

      user: {
        userId: "",
        username: "",
        email: "",
        profilePicture : "",
        showActivity: false,
        showFeedback: false
      }
    }
  },

  actions: {
    setToken(value) {
      this.sessionToken = value
    },
    setTokenExpires(value) {
        this.sessionTokenExpires = value
    },
    setShowActivity(value) {
      this.user.showActivity = value
    },
    setShowFeedback(value) {
      this.user.showFeedback = value
    },

    async fetchUserData() {
      await getUser()
          .then(response => {
            this.user = response.data
            this.user.profilePicture = getPictureFromID(this.user.userId);

          }).catch(error  => {
            console.warn("error", error)
          })
    },

    logoutUser() {
      localStorage.removeItem("sessionToken")
      localStorage.removeItem("user")
      this.setToken(null)
      this.setTokenExpires(null)
      useQuizStore().resetCurrentQuiz()
    }
  },

  getters: {
    getUserData() {return this.user},
    isAuth() {return this.sessionToken !== null},
    getToken() {return this.sessionToken},
    tokenExpired() {
      const expiryDate = new Date(this.sessionTokenExpires);
      // Compare with the current time
      return expiryDate.getTime() < Date.now();
    }
  },

  persist: {
    storage: sessionStorage
  }
})


export const useQuizStore = defineStore('storeQuiz', {

  state: () => {
    return {
      allQuiz: [],
      currentQuiz: {
        quizId: null,
        quizName: "",
        difficulty: "",
        quizDescription: "",
        adminId: null,
        feedback: [],
        collaborators: [],
        categories: [],
        questions: [
          {
            quizId: null,
            questionId: null,
            question: "",
            answer: "",
            type: "",
            choices: [],
          },
        ],
        keywords: [],
      },

      category_list: getAllCategories()
    }
  },

  actions: {

    async loadQuizzes(quizFilterDTO) {
      try {
        const response = await fetchFilteredQuizzes(quizFilterDTO);
        this.allQuizzes = [ ...response ];
        return this.allQuizzes;
      } catch (error) {
          console.error("Failed to load previous page:", error);
      }
    },

    async loadMyQuizzes() {
      try {
        const userId = useUserStore().user.userId;
        return await fetchAllQuizzesByUser(userId);
      } catch (error) {
        console.error("Failed to load previous page:", error);
      }
    },

    async filterAuthor(searchQuery) {
      try {
        const response = await fetchUserByUsername(searchQuery);
        return response;
      } catch (error) {
        console.error("Failed to load previous page:", error);
      }
    },

    isAdmin(adminId) {
      return this.currentQuiz.adminId === useUserStore().user.userId;
    },

    async deleteCurrentQuiz() {
      await deleteQuizById(this.currentQuiz.quizId)
          .then(response => {
          }).catch(error => {
            console.warn("error", error)
          })
    },

    async loadCategories() {
      await fetchCategories()
          .then(response => {
            this.category_list = response.categories;
          }).catch(error => {
            console.warn("error", error)
          })
    },

    async editQuestion(editedQuestion){
      const editQuestionDTO = {
        "quizId": editedQuestion.quizId,
        "questionId": editedQuestion.questionId,
        "question": editedQuestion.question,
        "type": editedQuestion.type,
        "choices": editedQuestion.choices,
        "answer" : editedQuestion.answer
      }


      await patchQuestion(editQuestionDTO)
          .then(response => {
            this.currentQuiz = response;
            return response;
          }).catch(error => {
            console.warn("error", error);
          });

      return editQuestionDTO
    },

    async addQuestion(newQuestion){
      const questionCreateDTO = {
        "quizId": this.currentQuiz.quizId,
        "question": newQuestion.question,
        "answer": newQuestion.answer,
        "type": newQuestion.type,
        "choices": newQuestion.choices
      };

      return await addQuestion(questionCreateDTO);
    },

    async deleteQuestion(question_id) {
      await deleteQuestionById(question_id)
          .then(response => {
            // console.log(response)
          }).catch(error => {
            console.warn("error", error)
          })
    },

    async deleteAuth(auth) {
      try {
        await removeCollaborator(auth.quizAuthorId);
        const index = this.currentQuiz.collaborators.findIndex(author => author.quizAuthorId === auth.quizAuthorId);
        if (index !== -1) {
          this.currentQuiz.collaborators.splice(index, 1);
        } else {
          console.warn("Author not found in collaborators array");
        }
      } catch (error) {
        console.error("Error deleting author:", error);
      }
    },


    async setCurrentQuizById(quiz) {
      this.currentQuiz = quiz;
      if (useUserStore().user.userId === this.currentQuiz.adminId){
        this.isAuth = true;
        this.isEditor = true;
      }
      return this.currentQuiz;
    },

    async updateCurrentQuiz(quizUpdateDTO) {
      await updateQuiz(quizUpdateDTO)
          .then(response => {
          }).catch(error => {
            console.warn("Error updating quiz:", error);
          });
    },

    async addAuthor(author) {
      const quizAuthorDTO = {
        "userId": author.userId,
        "quizId": this.currentQuiz.quizId
      };
      await addCollaborator(quizAuthorDTO)
          .then(response => {
            this.currentQuiz.collaborators.push(response)
          }).catch(error => {
            console.warn("error", error)
          })

      return this.currentQuiz.collaborators;
    },

    resetCurrentQuiz() {
      this.currentQuiz = null;
      this.isAuth = false;
      this.isEditor = false;
    },
  },

  persist: {
    storage: sessionStorage
  }
})

export const useQuizCreateStore = defineStore('storeQuizCreate', {
  state: () => {
    return {
      templateQuiz: {
        quizId: null,
        quizName: "TemplateQuiz",
        quizDifficulty: "Easy",
        quizDescription: "Template quiz, change the quiz as wanted",
        admin_id: null,
        feedbacks: [],
        collaborators: [],
        categories: [],
        questions: [
          {
            quizId: null,
            question: "What is your question?",
            answer: "John",
            type: "multiple_choice",
            choices: [
              { alternative: "pencil", isCorrect: false},
              { alternative: "book", isCorrect: false},
              { alternative: "John", isCorrect: true},
              { alternative: "quiz", isCorrect: false}
            ]
          },
          {
            quizId: null,
            question: "Are you 21 years old?",
            answer: "true",
            type: "true_false"
          },
          {
            quizId: null,
            question: "What is in the center of the Milky Way?",
            answer: "Black Hole",
            type: "multiple_choice",
            choices: [
              { alternative: "Sun", isCorrect: false },
              { alternative: "Earth", isCorrect: false },
              { alternative: "Venus", isCorrect: false },
              { alternative: "Black Hole", isCorrect: true }
            ]
          },
        ],
        keywords: [],
        Image: "https://via.placeholder.com/150",
      }
    }
  },

  actions: {
    async createQuiz(quiz) {
      let createdQuiz = null;
      this.templateQuiz.questions = quiz.questions;

      await createQuiz(quiz.quizName)
          .then(response => {
            createdQuiz = response;
            this.templateQuiz.quizId = response.quizId
          }).catch(error => {
            console.warn("Error creating quiz:", error);
          });

      const quizUpdateDTO = {
        "quizId": this.templateQuiz.quizId,
        "newName": createdQuiz.quizName,
        "newDescription": quiz.quizDescription,
        "difficulty": quiz.quizDifficulty.toUpperCase(),
      };


      const addQuestionPromises = [];
      this.templateQuiz.questions.forEach(question => {
        const questionCreateDTO = {
          "quizId": this.templateQuiz.quizId,
          "question": question.question,
          "answer": question.answer,
          "type": question.type.toUpperCase(),
          "choices": question.choices
        };
        addQuestionPromises.push(addQuestion(questionCreateDTO));
      });

      await Promise.all(addQuestionPromises)
          .then(responses => {
            createdQuiz = responses[responses.length - 1];
          }).catch(error => {
            console.warn("Error adding questions:", error);
          });

      const imgDTO = {
        "quizId" : createdQuiz.quizId,
        "quizImage": quiz.Image
      }

      await saveFile(imgDTO)
          .then(response => {
          }).catch(error => {
            console.warn("Error saving img:", error);
          });

      const keywordsDTO = {
        "quizId": quiz.quizId,
        "keywords": quiz.keywords
      };
      if(quiz.keywords && quiz.keywords.length !== 0 && quiz.quizId !== null) {
        await addKeyword(keywordsDTO);
      }

      this.templateQuiz.keywords = [];

      const addCategoryPromises = [];
      this.templateQuiz.categories.forEach(category => {
        const QuizCategoryCreateDTO = {
          "quizId": this.templateQuiz.quizId,
          "categoryId": category.categoryId
        };
      });
      this.templateQuiz.categories = [];

      await Promise.all(addCategoryPromises)
          .then(responses => {
            // console.log("Categories added:", responses);
          }).catch(error => {
            console.warn("Error adding categories:", error);
          });

      await updateQuiz(quizUpdateDTO)
          .then(response => {
            // console.log(response);
            createdQuiz = response;
          }).catch(error => {
            console.warn("Error updating quiz:", error);
          });
    },
    generateTemplate() {
      return {
        quizId: null,
            quizName: "TemplateQuiz",
            quizDifficulty: "Easy",
            quizDescription: "Template quiz, change the quiz as wanted",
            admin_id: null,
            feedbacks: [],
            collaborators: [],
            categories: [],
            questions: [
          {
            quizId: null,
            question: "What is your question?",
            answer: "John",
            type: "multiple_choice",
            choices: [
              { alternative: "pencil", isCorrect: false},
              { alternative: "book", isCorrect: false},
              { alternative: "John", isCorrect: true},
              { alternative: "quiz", isCorrect: false}
            ]
          },
          {
            quizId: null,
            question: "Are you 21 years old?",
            answer: "true",
            type: "true_false",
            choices: null
          },
          {
            quizId: null,
            question: "What is in the center of the Milky Way?",
            answer: "Black Hole",
            type: "multiple_choice",
            choices: [
              { alternative: "Sun", isCorrect: false },
              { alternative: "Earth", isCorrect: false },
              { alternative: "Venus", isCorrect: false },
              { alternative: "Black Hole", isCorrect: true }
            ]
          },
        ],
            keywords: [],
            Image: "https://via.placeholder.com/150",
      }
    },
    resetTemplate() {
      this.templateQuiz = this.generateTemplate();
    }
  },
  persist: {
    storage: sessionStorage
  }
})
