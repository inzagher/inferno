create schema if not exists library;
create table library.authors (id bigserial primary key, full_name varchar(255) not null, book_id bigint not null);
create table library.books (id bigint primary key, version bigint, title varchar(255) not null);
alter table library.authors add constraint authors_book_id_fkey foreign key (book_id) references library.books;