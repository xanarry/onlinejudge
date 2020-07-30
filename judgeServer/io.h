#ifndef IO_H
#define IO_H

#include <unistd.h>
#include "global.h"

void write_n_byte(int fd, char *buf, const ssize_t n);
void read_n_byte_to_file(int fd_from, int fd_to, const ssize_t n);
void read_n_byte_to_buf(int fd, char *buf, ssize_t n);
char *trim_tail(char *string);
int createSorcecodeFile(struct request *req, char *tmpdir);

#endif