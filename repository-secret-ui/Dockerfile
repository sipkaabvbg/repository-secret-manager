FROM node:20-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 8080
CMD ["npx", "ui5", "serve", "--port", "8080", "--host", "0.0.0.0"]