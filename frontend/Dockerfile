FROM nginx:alpine
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY dist/frontend/browser /usr/share/nginx/html
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]
