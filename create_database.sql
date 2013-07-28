--------------------------------------------------------------------------------
-- Table   :   text
--------------------------------------------------------------------------------
CREATE SEQUENCE text_txt_id_seq INCREMENT 1 START 1
;

CREATE TABLE text ( 
	txt_id integer DEFAULT NEXTVAL('text_txt_id_seq'::TEXT) NOT NULL,
	txt_code varchar(10) NOT NULL,
	txt_title varchar(100) NOT NULL,
	txt_author varchar(50) NOT NULL,
	txt_text text,
	txt_keywords_mac text,
	txt_keywords_textrank text
)
;

ALTER TABLE text ADD CONSTRAINT PK_Text 
	PRIMARY KEY (txt_id)
;