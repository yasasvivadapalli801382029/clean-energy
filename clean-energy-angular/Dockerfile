# Step 1: Use the official Node.js image to build the Angular app
FROM node:18 AS build

# Set the working directory for the frontend
WORKDIR /app

# Copy package.json and package-lock.json to install dependencies
COPY ./package*.json ./



# Install Angular CLI and project dependencies
RUN npm install -g @angular/cli && npm install

# Copy the Angular project files
COPY . .

# Build the Angular project for production
RUN ng build --configuration=production

# Step 2: Use Nginx to serve the built Angular app
FROM nginx:alpine

# Copy the built Angular app from the previous stage
COPY --from=build /app/dist/clean-energy-angular /usr/share/nginx/html

# Copy nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80 for the frontend
EXPOSE 80

# Optional: Default command to run Nginx
CMD ["nginx", "-g", "daemon off;"]