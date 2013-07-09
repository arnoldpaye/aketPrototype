/*******************************TEXT*******************************************/
CREATE SEQUENCE Text_txt_id_seq INCREMENT 1 START 1
;

CREATE TABLE Text ( 
	txt_id integer DEFAULT NEXTVAL('Text_txt_id_seq'::TEXT) NOT NULL,
	txt_title varchar(100) NOT NULL,
	txt_author varchar(50) NOT NULL,
	txt_text text
)
;

ALTER TABLE Text ADD CONSTRAINT PK_Text 
	PRIMARY KEY (txt_id)
;