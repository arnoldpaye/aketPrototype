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
--------------------------------------------------------------------------------
-- Table   :   evaluation
--------------------------------------------------------------------------------
CREATE SEQUENCE evaluation_ev_id_seq INCREMENT 1 START 1
;

CREATE TABLE evaluation ( 
	ev_id integer DEFAULT NEXTVAL('evaluation_ev_id_seq'::TEXT) NOT NULL,
	ev_txt_id integer NOT NULL,
	ev_precision decimal(10,2),
	ev_recall decimal(10,2),
	ev_fmeasure decimal(10,2)
)
;

ALTER TABLE evaluation ADD CONSTRAINT PK_evaluation 
	PRIMARY KEY (ev_id)
;


ALTER TABLE evaluation ADD CONSTRAINT FK_evaluation_text 
	FOREIGN KEY (ev_txt_id) REFERENCES text (txt_id)
;