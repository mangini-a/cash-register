package com.mpp.jooq;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Target;

import com.mpp.dbinit.CreateDB;

/*
 * Generazione automatica del codice Java per accedere al DB, nella directory src-generated.
 */
public class GenerateCode {

	public static void main(String[] args) throws Exception {
		Jdbc JDBC = new Jdbc().withDriver("org.sqlite.JDBC").withUrl(CreateDB.DB_URL);
		Database database = new Database().withName("org.jooq.meta.sqlite.SQLiteDatabase").withIncludes(".*").withExcludes("");
		Target target = new Target().withPackageName("com.mpp.jooq.generated").withDirectory("src-generated/");
		Generator generator = new Generator().withDatabase(database).withTarget(target);
		//generator.getGenerate().setPojos(true);
		Configuration configuration = new Configuration()
				.withJdbc(JDBC)
				.withGenerator(generator);
		GenerationTool.generate(configuration);	
	}

}
