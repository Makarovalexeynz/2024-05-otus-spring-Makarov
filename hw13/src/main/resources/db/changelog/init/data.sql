insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(text, book_id)
values ('comment_1', 1), ('comment_2', 2), ('comment_3', 3);

insert into users(username, password, authority)
values ('user', '$2a$10$WNjeMNEiyFIszBkl.FdCEun.T1q/NuJL26xloV65w8CKjh6ciW3Xe', 'USER'), ('admin', '$2a$10$mlXzmxPCXOwMCyV7RObAw.9BSfa2MIiVIZIzRDbxFqqYYxVJW6FNq', 'ADMIN');
