import { defineStore } from 'pinia'
import {
  getUser
} from "@/services/UserService.js";
import {getPictureFromUser} from "@/services/ImageService.js";

import {
  addCollaborator,
  addQuestion,
  createQuiz, deleteQuizById, removeCollaborator, updateQuiz
} from "@/services/QuizService.js"

export const useUserStore = defineStore('storeUser', {

  state: () => {
    return{
      sessionToken: null,
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
            getPictureFromUser('2', response.data.profilePicture).then(response =>{
              this.user.profilePicture = `data:${response.data.contentType};base64,${response.data.image}`;
            }).catch(e => {
              console.log(e)
            });
          }).catch(error  => {
            console.warn("error", error)
          })
    },

    logoutUser() {
      localStorage.removeItem("sessionToken")
      localStorage.removeItem("user")
      this.setToken(null)
      useQuizStore().resetCurrentQuiz()
      //TODO: invalidate token in backend.
    }
  },

  getters: {
    getUserData() {return this.user},
    isAuth() {return this.sessionToken !== null}, //TODO: should check if token is valid...
    getToken() {return this.sessionToken}
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
        quizDifficulty: "",
        quizDescription: "",
        admin_id: null,
        feedback: [],
        collaborators: [],
        categories: [],
        questions: [
          {
            quizId: null,
            question: "",
            answer: "",
            type: "",
            choices: [],
          },
        ],
        keywords: [],
        Image: "",
      },
    }
  },

  actions: {
    async loadQuizzes(searchword, difficulty, pageIndex, numberOfQuizzes) {
      /*
      try {
        const response = await fetchQuizzes(searchword, difficulty, pageIndex, numberOfQuizzes);
        this.allQuizzes = [ ...response.content ];
        return this.allQuizzes;
      } catch (error) {
          console.error("Failed to load previous page:", error);
          pageIndex.value--;
      }
       */
    },

    async isAdmin(quizAdminId) {
      return quizAdminId === useUserStore().user.userId;
    },

    async isCollaborator() {

    },

    async deleteCurrentQuiz() {
      await deleteQuizById(this.currentQuiz.quizId)
          .then(response => {
            console.log(response)
          }).catch(error => {
            console.warn("error", error)
          })
    },

    async editQuestion(editedQuestion){
      /*
      TODO: axioscall
      editQuestionById(this.currentQuiz.quizId, editedQuestion.id)
      */
    },

    async addQuestion(newQuestion){
      const questionCreateDTO = {
        "quizId": this.currentQuiz.quizId,
        "question": newQuestion.question,
        "answer": newQuestion.answer,
        "type": newQuestion.type.toUpperCase(),
        "choices": newQuestion.choices
      };

      console.log(questionCreateDTO)

      await addQuestion(questionCreateDTO)
          .then(responses => {
            console.log(responses);
          }).catch(error => {
            console.warn("error", error);
          });
    },

    async addQuiz() {
      /*
      TODO: axioscall
      addQuizById(user.id, this.currentQuiz.quizId)
      */
    },

    async deleteQuestion(question_id) {
      for (let index = 0; index < this.currentQuiz.question_list.length; index++) {
        if (question_id === this.currentQuiz.question_list[index].id) {
          this.currentQuiz.question_list.splice(index, 1);
          /*
          TODO: fjerne q i backend
          response = deleteQuestion(q.id)
          this.currentQuiz.Questions = response;
           */
          break;
        }
      }
      return this.currentQuiz.question_list;
    },

    async deleteAuth(auth) {

      await removeCollaborator(auth.username)
          .then(response => {
            console.log(response)
          }).catch(error => {
            console.warn("error", error)
          })

      return this.currentQuiz.collaborators;
    },

    async setCurrentQuizById(quiz) {
      console.log(quiz)
      this.currentQuiz = quiz;
      if (useUserStore().user.userId === this.currentQuiz.QuizId){
        this.isAuth = true;
        this.isEditor = true;
      }
      return this.currentQuiz;
    },

    async addAuthor(newAuthor) {
      this.currentQuiz.collaborators.push({id: 4, username: newAuthor.username});
      const quizAuthorDTO = {
        username: newAuthor.username,
        quizId: this.currentQuiz.quizId
      };
      await addCollaborator(quizAuthorDTO)
          .then(response => {
            console.log(response)
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
})

export const useQuizCreateStore = defineStore('storeQuizCreate', {
  state: () => {
    return {
      templateQuiz: {
        QuizId: null,
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
            choices: [
              {alternative: "true", isCorrect: true},
              {alternative: "false", isCorrect: false}
            ]
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
    async deleteTag(tag) {

    },


    async addTag(newTag) {

    },

    async createQuiz(questions) {
      let createdQuiz = null;
      this.templateQuiz.questions = questions.value;

      await createQuiz(this.templateQuiz.quizName)
          .then(response => {
            console.log(response)
            createdQuiz = response;
          }).catch(error => {
            console.warn("error", error)
          })

      const quizUpdateDTO = {
        "quizId": createdQuiz.quizId,
        "newName": createdQuiz.quizName,
        "newDescription": this.templateQuiz.quizDescription,
        "difficulty": this.templateQuiz.quizDifficulty.toUpperCase(),
      }

      // Array to hold promises for adding questions
      const addQuestionPromises = [];

      // Loop through each question
      console.log(this.templateQuiz.questions)
      this.templateQuiz.questions.forEach(question => {
        const questionCreateDTO = {
          "quizId": createdQuiz.quizId,
          "question": question.question,
          "answer": question.answer,
          "type": question.type.toUpperCase(),
          "choices": question.choices
        };
        addQuestionPromises.push(addQuestion(questionCreateDTO));
      });

      // Execute all promises to add questions
      await Promise.all(addQuestionPromises)
          .then(responses => {
            createdQuiz = responses[responses.length - 1];
          }).catch(error => {
            console.warn("error", error);
          });

      // Update the quiz after adding all questions
      await updateQuiz(quizUpdateDTO)
          .then(response => {
            console.log(response);
            createdQuiz = response;
          }).catch(error => {
            console.warn("error", error);
          });
    },
  },
})
