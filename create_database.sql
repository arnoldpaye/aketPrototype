/******************************************************************************
Table   :   Text
*******************************************************************************/
CREATE SEQUENCE Text_txt_id_seq INCREMENT 1 START 1
;

CREATE TABLE Text ( 
	txt_id integer DEFAULT NEXTVAL('Text_txt_id_seq'::TEXT) NOT NULL,
	txt_title varchar(100) NOT NULL,
	txt_author varchar(50) NOT NULL,
	txt_text_raw text,
	txt_text_classified text,
	txt_keywords_mac text,
	txt_keywords_textrank text
)
;

ALTER TABLE Text ADD CONSTRAINT PK_Text 
	PRIMARY KEY (txt_id)
;