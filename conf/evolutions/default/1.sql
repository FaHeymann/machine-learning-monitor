# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table algorithm (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  description                   varchar(255) not null,
  endpoint                      varchar(255) not null,
  user_id                       integer not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_algorithm primary key (id)
);

create table feature (
  id                            integer auto_increment not null,
  result                        double not null,
  feature_set_id                integer not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_feature primary key (id)
);

create table feature_entry (
  id                            integer auto_increment not null,
  feature_id                    integer not null,
  feature_label_id              integer not null,
  value                         varchar(255) not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_feature_entry primary key (id)
);

create table feature_label (
  id                            integer auto_increment not null,
  feature_set_id                integer not null,
  value                         varchar(255) not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_feature_label primary key (id)
);

create table feature_set (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  description                   varchar(255) not null,
  user_id                       integer not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_feature_set primary key (id)
);

create table parameter (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  type                          integer not null,
  algorithm_id                  integer not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint ck_parameter_type check (type in (0,1,2,3)),
  constraint pk_parameter primary key (id)
);

create table parameter_enum_value (
  id                            integer auto_increment not null,
  value                         varchar(255) not null,
  parameter_id                  integer not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_parameter_enum_value primary key (id)
);

create table parameter_test_value (
  id                            integer auto_increment not null,
  result_set_id                 integer not null,
  parameter_id                  integer not null,
  string_value                  varchar(255),
  int_value                     integer,
  double_value                  double,
  enum_value_id                 integer,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_parameter_test_value primary key (id)
);

create table result (
  id                            integer auto_increment not null,
  expected                      double not null,
  actual                        double not null,
  result_set_id                 integer not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_result primary key (id)
);

create table result_set (
  id                            integer auto_increment not null,
  feature_set_id                integer not null,
  algorithm_id                  integer not null,
  test_matrix_id                integer,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_result_set primary key (id)
);

create table test_matrix (
  id                            integer auto_increment not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_test_matrix primary key (id)
);

create table user (
  id                            integer auto_increment not null,
  email                         varchar(255) not null,
  name                          varchar(255) not null,
  password                      varchar(255) not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id)
);

alter table algorithm add constraint fk_algorithm_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_algorithm_user_id on algorithm (user_id);

alter table feature add constraint fk_feature_feature_set_id foreign key (feature_set_id) references feature_set (id) on delete restrict on update restrict;
create index ix_feature_feature_set_id on feature (feature_set_id);

alter table feature_entry add constraint fk_feature_entry_feature_id foreign key (feature_id) references feature (id) on delete restrict on update restrict;
create index ix_feature_entry_feature_id on feature_entry (feature_id);

alter table feature_entry add constraint fk_feature_entry_feature_label_id foreign key (feature_label_id) references feature_label (id) on delete restrict on update restrict;
create index ix_feature_entry_feature_label_id on feature_entry (feature_label_id);

alter table feature_label add constraint fk_feature_label_feature_set_id foreign key (feature_set_id) references feature_set (id) on delete restrict on update restrict;
create index ix_feature_label_feature_set_id on feature_label (feature_set_id);

alter table feature_set add constraint fk_feature_set_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_feature_set_user_id on feature_set (user_id);

alter table parameter add constraint fk_parameter_algorithm_id foreign key (algorithm_id) references algorithm (id) on delete restrict on update restrict;
create index ix_parameter_algorithm_id on parameter (algorithm_id);

alter table parameter_enum_value add constraint fk_parameter_enum_value_parameter_id foreign key (parameter_id) references parameter (id) on delete restrict on update restrict;
create index ix_parameter_enum_value_parameter_id on parameter_enum_value (parameter_id);

alter table parameter_test_value add constraint fk_parameter_test_value_result_set_id foreign key (result_set_id) references result_set (id) on delete restrict on update restrict;
create index ix_parameter_test_value_result_set_id on parameter_test_value (result_set_id);

alter table parameter_test_value add constraint fk_parameter_test_value_parameter_id foreign key (parameter_id) references parameter (id) on delete restrict on update restrict;
create index ix_parameter_test_value_parameter_id on parameter_test_value (parameter_id);

alter table parameter_test_value add constraint fk_parameter_test_value_enum_value_id foreign key (enum_value_id) references parameter_enum_value (id) on delete restrict on update restrict;
create index ix_parameter_test_value_enum_value_id on parameter_test_value (enum_value_id);

alter table result add constraint fk_result_result_set_id foreign key (result_set_id) references result_set (id) on delete restrict on update restrict;
create index ix_result_result_set_id on result (result_set_id);

alter table result_set add constraint fk_result_set_feature_set_id foreign key (feature_set_id) references feature_set (id) on delete restrict on update restrict;
create index ix_result_set_feature_set_id on result_set (feature_set_id);

alter table result_set add constraint fk_result_set_algorithm_id foreign key (algorithm_id) references algorithm (id) on delete restrict on update restrict;
create index ix_result_set_algorithm_id on result_set (algorithm_id);

alter table result_set add constraint fk_result_set_test_matrix_id foreign key (test_matrix_id) references test_matrix (id) on delete restrict on update restrict;
create index ix_result_set_test_matrix_id on result_set (test_matrix_id);


# --- !Downs

alter table algorithm drop constraint if exists fk_algorithm_user_id;
drop index if exists ix_algorithm_user_id;

alter table feature drop constraint if exists fk_feature_feature_set_id;
drop index if exists ix_feature_feature_set_id;

alter table feature_entry drop constraint if exists fk_feature_entry_feature_id;
drop index if exists ix_feature_entry_feature_id;

alter table feature_entry drop constraint if exists fk_feature_entry_feature_label_id;
drop index if exists ix_feature_entry_feature_label_id;

alter table feature_label drop constraint if exists fk_feature_label_feature_set_id;
drop index if exists ix_feature_label_feature_set_id;

alter table feature_set drop constraint if exists fk_feature_set_user_id;
drop index if exists ix_feature_set_user_id;

alter table parameter drop constraint if exists fk_parameter_algorithm_id;
drop index if exists ix_parameter_algorithm_id;

alter table parameter_enum_value drop constraint if exists fk_parameter_enum_value_parameter_id;
drop index if exists ix_parameter_enum_value_parameter_id;

alter table parameter_test_value drop constraint if exists fk_parameter_test_value_result_set_id;
drop index if exists ix_parameter_test_value_result_set_id;

alter table parameter_test_value drop constraint if exists fk_parameter_test_value_parameter_id;
drop index if exists ix_parameter_test_value_parameter_id;

alter table parameter_test_value drop constraint if exists fk_parameter_test_value_enum_value_id;
drop index if exists ix_parameter_test_value_enum_value_id;

alter table result drop constraint if exists fk_result_result_set_id;
drop index if exists ix_result_result_set_id;

alter table result_set drop constraint if exists fk_result_set_feature_set_id;
drop index if exists ix_result_set_feature_set_id;

alter table result_set drop constraint if exists fk_result_set_algorithm_id;
drop index if exists ix_result_set_algorithm_id;

alter table result_set drop constraint if exists fk_result_set_test_matrix_id;
drop index if exists ix_result_set_test_matrix_id;

drop table if exists algorithm;

drop table if exists feature;

drop table if exists feature_entry;

drop table if exists feature_label;

drop table if exists feature_set;

drop table if exists parameter;

drop table if exists parameter_enum_value;

drop table if exists parameter_test_value;

drop table if exists result;

drop table if exists result_set;

drop table if exists test_matrix;

drop table if exists user;

