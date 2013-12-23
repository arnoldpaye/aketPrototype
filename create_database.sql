    --------------------------------------------------------------------------------
    -- Table   :   career
    --------------------------------------------------------------------------------
    CREATE SEQUENCE career_cr_id_seq INCREMENT 1 START 1
    ;

    CREATE TABLE career ( 
            cr_id integer DEFAULT NEXTVAL('career_cr_id_seq'::TEXT) NOT NULL,
            cr_name varchar(30) NOT NULL
    )
    ;

    ALTER TABLE career ADD CONSTRAINT PK_career 
            PRIMARY KEY (cr_id)
    ;
    --------------------------------------------------------------------------------
    -- Table   :   text
    --------------------------------------------------------------------------------
    CREATE SEQUENCE text_txt_id_seq INCREMENT 1 START 1
    ;

    CREATE TABLE text ( 
            txt_id integer DEFAULT NEXTVAL('text_txt_id_seq'::TEXT) NOT NULL,
            txt_cr_id integer NOT NULL,
            txt_code varchar(15) NOT NULL,
            txt_title varchar(300) NOT NULL,
            txt_author varchar(150) NOT NULL,
            txt_keyword text,
            txt_text text
    )
    ;

    ALTER TABLE text ADD CONSTRAINT PK_Text 
            PRIMARY KEY (txt_id)
    ;


    ALTER TABLE text ADD CONSTRAINT FK_text_career 
            FOREIGN KEY (txt_cr_id) REFERENCES career (cr_id)
    ;
    --------------------------------------------------------------------------------
    -- Table   :   keyword
    --------------------------------------------------------------------------------
    CREATE SEQUENCE keyword_kw_id_seq INCREMENT 1 START 1
    ;

    CREATE TABLE keyword ( 
            kw_id integer DEFAULT NEXTVAL('keyword_kw_id_seq'::TEXT) NOT NULL,
            kw_txt_id integer NOT NULL,
            kw_value text,
            kw_source integer NOT NULL
    )
    ;

    ALTER TABLE keyword ADD CONSTRAINT PK_keyword 
            PRIMARY KEY (kw_id)
    ;


    ALTER TABLE keyword ADD CONSTRAINT FK_keyword_text 
            FOREIGN KEY (kw_txt_id) REFERENCES text (txt_id)
    ;
    --------------------------------------------------------------------------------
    -- Table   :   evaluation
    --------------------------------------------------------------------------------
    CREATE SEQUENCE evaluation_ev_id_seq INCREMENT 1 START 1
    ;

    CREATE TABLE evaluation ( 
            ev_id integer DEFAULT NEXTVAL('evaluation_ev_id_seq'::TEXT) NOT NULL,
            ev_kw_id integer NOT NULL,
            ev_precision decimal(10,2),
            ev_recall decimal(10,2),
            ev_fmeasure decimal(10,2)
    )
    ;

    ALTER TABLE evaluation ADD CONSTRAINT PK_evaluation 
            PRIMARY KEY (ev_id)
    ;


    ALTER TABLE evaluation ADD CONSTRAINT FK_evaluation_keyword 
            FOREIGN KEY (ev_kw_id) REFERENCES keyword (kw_id)
    ;   