FROM node:20-alpine3.18 as BUILD

#mkdir app and cd app
WORKDIR /app
# Copy from the source to the container (first path is relative to the Dockerfile, second is relative to root on the container)
COPY package.json package-lock.json  .
RUN npm install
COPY . .
RUN npm run build
FROM nginx:1.21.6-alpine
COPY --from=BUILD /app/build/ /usr/share/nginx/html/
CMD [ "nginx", "-g", "daemon off;"]