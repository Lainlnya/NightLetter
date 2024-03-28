from sqlalchemy import *
from sqlalchemy.orm import sessionmaker, scoped_session
from sqlalchemy.ext.declarative import declarative_base
import env

DATABASE ='mysql://%s:%s@%s/%s?charset=utf8'%(
    env.MYSQL_USER,
    env.MYSQL_PWD,
    env.MYSQL_HOST,
    env.MYSQL_DATABASE
)

ENGINE = create_engine(
    DATABASE,
    echo=True
)

session = scoped_session(
    sessionmaker(
        autocommit=False,
        autoflush=False,
        bind=ENGINE
    )
)

Base = declarative_base()
Base.query = session.query_property()
