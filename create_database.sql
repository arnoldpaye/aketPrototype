--------------------------------------------------------------------------------
-- Table   :   text
--------------------------------------------------------------------------------
CREATE SEQUENCE text_txt_id_seq INCREMENT 1 START 1
;

CREATE TABLE text ( 
	txt_id integer DEFAULT NEXTVAL('text_txt_id_seq'::TEXT) NOT NULL,
	txt_title varchar(100) NOT NULL,
	txt_author varchar(50) NOT NULL,
	txt_text text,
	txt_text_classified boolean,
	txt_keywords_mac text,
	txt_keywords_textrank text
)
;

ALTER TABLE text ADD CONSTRAINT PK_Text 
	PRIMARY KEY (txt_id)
;
--------------------------------------------------------------------------------
-- Table   :   word_tag
--------------------------------------------------------------------------------
CREATE SEQUENCE word_tag_wtg_id_seq INCREMENT 1 START 1
;

CREATE TABLE word_tag ( 
	wtg_id bigint DEFAULT NEXTVAL('word_tag_wtg_id_seq'::TEXT) NOT NULL,
	wtg_txt_id integer,
	wtg_value varchar(20),
	wtg_tag varchar(5)
)
;

ALTER TABLE word_tag ADD CONSTRAINT PK_word_tag 
	PRIMARY KEY (wtg_id)
;


ALTER TABLE word_tag ADD CONSTRAINT FK_word_tag_text 
	FOREIGN KEY (wtg_txt_id) REFERENCES text (txt_id)
;