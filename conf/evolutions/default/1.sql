# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table algorithm (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  description                   varchar(255) not null,
  endpoint                      varchar(255) not null,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint pk_algorithm primary key (id)
);

create table feature (
  id                            integer auto_increment not null,
  column_amount                 integer not null,
  serialized_data               varchar(255) not null,
  result                        double not null,
  feature_set_id                integer not null,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint pk_feature primary key (id)
);

create table feature_set (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  description                   varchar(255) not null,
  column_amount                 integer not null,
  serialized_labels             varchar(255) not null,
  user_id                       integer,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint pk_feature_set primary key (id)
);

create table result (
  id                            integer auto_increment not null,
  expected                      double not null,
  actual                        double not null,
  result_set_id                 integer not null,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint pk_result primary key (id)
);

create table result_set (
  id                            integer auto_increment not null,
  feature_set_id                integer not null,
  algorithm_id                  integer not null,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint pk_result_set primary key (id)
);

create table user (
  id                            integer auto_increment not null,
  email                         varchar(255) not null,
  name                          varchar(255) not null,
  password                      varchar(255) not null,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id)
);

alter table feature add constraint fk_feature_feature_set_id foreign key (feature_set_id) references feature_set (id) on delete restrict on update restrict;
create index ix_feature_feature_set_id on feature (feature_set_id);

alter table feature_set add constraint fk_feature_set_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_feature_set_user_id on feature_set (user_id);

alter table result add constraint fk_result_result_set_id foreign key (result_set_id) references result_set (id) on delete restrict on update restrict;
create index ix_result_result_set_id on result (result_set_id);

alter table result_set add constraint fk_result_set_feature_set_id foreign key (feature_set_id) references feature_set (id) on delete restrict on update restrict;
create index ix_result_set_feature_set_id on result_set (feature_set_id);

alter table result_set add constraint fk_result_set_algorithm_id foreign key (algorithm_id) references algorithm (id) on delete restrict on update restrict;
create index ix_result_set_algorithm_id on result_set (algorithm_id);


# --- !Downs

alter table feature drop foreign key fk_feature_feature_set_id;
drop index ix_feature_feature_set_id on feature;

alter table feature_set drop foreign key fk_feature_set_user_id;
drop index ix_feature_set_user_id on feature_set;

alter table result drop foreign key fk_result_result_set_id;
drop index ix_result_result_set_id on result;

alter table result_set drop foreign key fk_result_set_feature_set_id;
drop index ix_result_set_feature_set_id on result_set;

alter table result_set drop foreign key fk_result_set_algorithm_id;
drop index ix_result_set_algorithm_id on result_set;

drop table if exists algorithm;

drop table if exists feature;

drop table if exists feature_set;

drop table if exists result;

drop table if exists result_set;

drop table if exists user;

