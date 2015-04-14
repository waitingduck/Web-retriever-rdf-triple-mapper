package htmlwordtag;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.openrdf.OpenRDFException;
import org.openrdf.model.BNode;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sparql.SPARQLRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.memory.MemoryStore;


public class HtmlWordTag {

	private static final String CALAIS_URL = "http://api.opencalais.com/tag/rs/enrich";

    private File input;
    private File output;
    private HttpClient client;

    private PostMethod createPostMethod() {

        PostMethod method = new PostMethod(CALAIS_URL);

        // Set mandatory parameters
        method.setRequestHeader("x-calais-licenseID", "guuv7mvdatyk3d5yy6xab8d3");

        // Set input content type
        //method.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");
        method.setRequestHeader("Content-Type", "text/html; charset=UTF-8");
        //method.setRequestHeader("Content-Type", "text/raw; charset=UTF-8");

		// Set response/output format
        method.setRequestHeader("Accept", "xml/rdf");
        //method.setRequestHeader("Accept", "application/json");

        // Enable Social Tags processing
        method.setRequestHeader("enableMetadataType", "SocialTags");

        return method;
    }

	private void run() {
		try {
            if (input.isFile()) {
                postFile(input, createPostMethod());
            } else if (input.isDirectory()) {
                System.out.println("working on all files in " + input.getAbsolutePath());
                for (File file : input.listFiles()) {
                    if (file.isFile())
                        postFile(file, createPostMethod());
                    else
                        System.out.println("skipping "+file.getAbsolutePath());
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private void doRequest(File file, PostMethod method) {
        try {
            int returnCode = client.executeMethod(method);
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                System.err.println("The Post method is not implemented by this URI");
                // still consume the response body
                method.getResponseBodyAsString();
            } else if (returnCode == HttpStatus.SC_OK) {
                System.out.println("File post succeeded: " + file);
                saveResponse(file, method);
            } else {
                System.err.println("File post failed: " + file);
                System.err.println("Got code: " + returnCode);
                System.err.println("response: "+method.getResponseBodyAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
    }

    private void saveResponse(File file, PostMethod method) throws IOException {
        PrintWriter writer = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    method.getResponseBodyAsStream(), "UTF-8"));
            File out = new File(output, file.getName() + ".xml");
            writer = new PrintWriter(new BufferedWriter(new FileWriter(out)));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) try {writer.close();} catch (Exception ignored) {}
        }
    }

    private void postFile(File file, PostMethod method) throws IOException {
        method.setRequestEntity(new FileRequestEntity(file, null));
        doRequest(file, method);
    }
    
    private static void loadhtml(){
    	String current = System.getProperty("user.dir");
        System.out.println("Current working directory in Java : " + current);
        
        File dir = new File(current + "\\input");
    	if (!dir.exists()) {
    		if (dir.mkdir()) {
    			System.out.println("Directory is created!");
    		} else {
    			System.out.println("Failed to create directory!");
    		}
    	}
    	
    	File f = null;
    	 try{
             // create new file
             f = new File(current+"\\input\\website1.html");

             // tries to create new file in the system
             if(f.exists()){
            	 if(f.delete()){
            		 System.out.println("file Website1.html is already exist.");
            		 System.out.println("file Website1.html has been delete.");
            		 if(f.createNewFile()){
                    	 System.out.println("create Webstie1.html success");
                     }
                     else{
                    	 System.out.println("fail to create Webstie1.html");
                     }
            	 }
            	 else{
            		 System.out.println("fail to delete Website1.html.");
            	 }
             }
             else{
            	 if(f.createNewFile()){
                	 System.out.println("create Webstie1.html success");
                 }
                 else{
                	 System.out.println("fail to create Webstie1.html");
                 }
             }
             
             PrintWriter outputStream = null;
             try{
            	 outputStream = new PrintWriter(new FileOutputStream(current+"\\input\\website1.html"));
             }
             catch(FileNotFoundException e){
            	 System.out.println("Error to find file webstie1.html");
            	 System.exit(0);
             }
             URL website = new URL("http://www.engadget.com/2014/11/23/hoverboard-cardboard-robots-toyota-prius-camper/");
             URLConnection connection = website.openConnection();
             BufferedReader in = new BufferedReader(
                                     new InputStreamReader(
                                         connection.getInputStream()));

             StringBuilder response = new StringBuilder();
             String inputLine;

             while ((inputLine = in.readLine()) != null) 
                 response.append(inputLine);

             in.close();
             outputStream.println(response);
             outputStream.close();
                
          }catch(Exception e){
             e.printStackTrace();
          }
    	 
    	 File f2 = null;
    	 try{
             // create new file
             f2 = new File(current+"\\input\\website2.html");

             // tries to create new file in the system
             if(f2.exists()){
            	 if(f2.delete()){
            		 System.out.println("file Website2.html is already exist.");
            		 System.out.println("file Website2.html has been delete.");
            		 if(f.createNewFile()){
                    	 System.out.println("create Webstie2.html success");
                     }
                     else{
                    	 System.out.println("fail to create Webstie2.html");
                     }
            	 }
            	 else{
            		 System.out.println("fail to delete Website2.html.");
            	 }
             }
             else{
            	 if(f2.createNewFile()){
                	 System.out.println("create Webstie2.html success");
                 }
                 else{
                	 System.out.println("fail to create Webstie2.html");
                 }
             }
             
             PrintWriter outputStream2 = null;
             try{
            	 outputStream2 = new PrintWriter(new FileOutputStream(current+"\\input\\website2.html"));
             }
             catch(FileNotFoundException e){
            	 System.out.println("Error to find file webstie2.html");
            	 System.exit(0);
             }
             URL website = new URL("http://www.engadget.com/2014/11/21/net-neutrality-lawsuit/");
             URLConnection connection = website.openConnection();
             BufferedReader in = new BufferedReader(
                                     new InputStreamReader(
                                         connection.getInputStream()));

             StringBuilder response = new StringBuilder();
             String inputLine;

             while ((inputLine = in.readLine()) != null) 
                 response.append(inputLine);

             in.close();
             outputStream2.println(response);
             outputStream2.close();
                
          }catch(Exception e){
             e.printStackTrace();
          }
    	
    }


	
	
	public static void main(String[] args) throws RepositoryException, MalformedQueryException, QueryEvaluationException {
		//get current path
		String current = System.getProperty("user.dir");
		//get html file from internet
		loadhtml();
		//make director for output
		verifyArgs();
		//translate html file to rdf
		HtmlWordTag httpClientPost = new HtmlWordTag();
        httpClientPost.input = new File("input");
        httpClientPost.output = new File("output");
        httpClientPost.client = new HttpClient();
        httpClientPost.client.getParams().setParameter("http.useragent", "Calais Rest Client");

        httpClientPost.run();
	
		//create main memory repository
		Repository repo = new SailRepository(new MemoryStore());
		repo.initialize();
		
		File file = new File(current+"\\output\\website1.html.xml");

		RepositoryConnection con = repo.getConnection();
		try {
		   con.add(file, null, RDFFormat.RDFXML);
		}  
		catch (OpenRDFException e) {
		   // handle exception
		}
		catch (java.io.IOException e) {
		   // handle io exception
		}
		
		System.out.println(con.isEmpty());

		//query entire repostiory
		String queryString = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX c: <http://s.opencalais.com/1/type/em/e/>\n"
				+ "PREFIX p: <http://s.opencalais.com/1/pred/>\n"
				+ "PREFIX geo: <http://s.opencalais.com/1/type/er/Geo/>\n"
				

				+ "SELECT  distinct ?s ?n\n"
				+ "WHERE {\n"
				+ "{  ?s rdf:type c:Organization.\n"
				+ "   ?s p:name ?n.\n}"
				+ "  UNION \n"
				+ "{  ?s rdf:type c:Person.\n"
				+ "   ?s p:name ?n.\n}"
				+ "  UNION \n"
				+ "{  ?s rdf:type geo:City.\n"
				+ "   ?s p:name ?n.\n}"
				+ "}";
		
		  //System.out.println(queryString);

			
		  //insert query through sparql repository connection
		  TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
		  TupleQueryResult result = tupleQuery.evaluate();
		  
		  File queryresultdir = new File(current + "\\queryresult");
	    	if (!queryresultdir.exists()) {
	    		if (queryresultdir.mkdir()) {
	    			System.out.println("Directory is created!");
	    		} else {
	    			System.out.println("Failed to create directory!");
	    		}
	    	}
	    	
	    	File queryresult = null;
	    	 try{
	             // create new file
	    		 queryresult = new File(current+"\\queryresult\\queryresult1.txt");

	             // tries to create new file in the system
	             if(queryresult.exists()){
	            	 if(queryresult.delete()){
	            		 System.out.println("file queryresult1.txt is already exist.");
	            		 System.out.println("file queryresult1.txt has been delete.");
	            		 if(queryresult.createNewFile()){
	                    	 System.out.println("create queryresult1.txt success");
	                     }
	                     else{
	                    	 System.out.println("fail to create queryresult1.txt");
	                     }
	            	 }
	            	 else{
	            		 System.out.println("fail to delete queryresult1.txt.");
	            	 }
	             }
	             else{
	            	 if(queryresult.createNewFile()){
	                	 System.out.println("create queryresult1.txt success");
	                 }
	                 else{
	                	 System.out.println("fail to create queryresult1.txt");
	                 }
	             }
	             
	             
	    	 }catch(Exception e){
	             e.printStackTrace();
	          }
		  
		  try {
			  PrintWriter outputStream = null;
	             try{
	            	 outputStream = new PrintWriter(new FileOutputStream(current+"\\queryresult\\queryresult1.txt"));
	             }
	             catch(FileNotFoundException e){
	            	 System.out.println("Error to find file queryresult1.txt");
	            	 System.exit(0);
	             }
			    //go through all triple in sparql repository
	            while (result.hasNext()) {  // iterate over the result
					BindingSet bindingSet = result.next();
					Value valueOfS = bindingSet.getValue("s");
					Value valueOfN = bindingSet.getValue("n");
					
					System.out.println(valueOfS + " " + valueOfN);

		             outputStream.println(valueOfS + " " + valueOfN);
		             
				
	           }
	             outputStream.close();
		  }
		  finally {
		      result.close();
		  }
		//create main memory repository
			Repository repo2 = new SailRepository(new MemoryStore());
			repo2.initialize();
			
			File file2 = new File(current+"\\output\\website2.html.xml");

			RepositoryConnection con2 = repo2.getConnection();
			try {
			   con2.add(file2, null, RDFFormat.RDFXML);
			}  
			catch (OpenRDFException e) {
			   // handle exception
			}
			catch (java.io.IOException e) {
			   // handle io exception
			}
			
			System.out.println(con2.isEmpty());

			//query entire repostiory
			String queryString2 = 
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
					+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
					+ "PREFIX c: <http://s.opencalais.com/1/type/em/e/>\n"
					+ "PREFIX p: <http://s.opencalais.com/1/pred/>\n"
					+ "PREFIX geo: <http://s.opencalais.com/1/type/er/Geo/>\n"
					
					+ "SELECT  distinct ?s ?n\n"
					+ "WHERE {\n"
					+ "{  ?s rdf:type c:Organization.\n"
					+ "   ?s p:name ?n.\n}"
					+ "  UNION \n"
					+ "{  ?s rdf:type c:Person.\n"
					+ "   ?s p:name ?n.\n}"
					+ "  UNION \n"
					+ "{  ?s rdf:type geo:City.\n"
					+ "   ?s p:name ?n.\n}"
					+ "}";

			
			  //System.out.println(queryString2);

			  
				
			  //insert query through sparql repository connection
			  TupleQuery tupleQuery2 = con2.prepareTupleQuery(QueryLanguage.SPARQL, queryString2);
			  TupleQueryResult result2 = tupleQuery2.evaluate();
			  
			
		    	File queryresult2 = null;
		    	 try{
		             // create new file
		    		 queryresult2 = new File(current+"\\queryresult\\queryresult2.txt");

		             // tries to create new file in the system
		             if(queryresult2.exists()){
		            	 if(queryresult2.delete()){
		            		 System.out.println("file queryresult2.txt is already exist.");
		            		 System.out.println("file queryresult2.txt has been delete.");
		            		 if(queryresult2.createNewFile()){
		                    	 System.out.println("create queryresult2.txt success");
		                     }
		                     else{
		                    	 System.out.println("fail to create queryresult2.txt");
		                     }
		            	 }
		            	 else{
		            		 System.out.println("fail to delete queryresult2.txt.");
		            	 }
		             }
		             else{
		            	 if(queryresult2.createNewFile()){
		                	 System.out.println("create queryresult2.txt success");
		                 }
		                 else{
		                	 System.out.println("fail to create queryresult2.txt");
		                 }
		             }
		             
		             
		    	 }catch(Exception e){
		             e.printStackTrace();
		          }
			  
			  try {
				  PrintWriter outputStream2 = null;
		             try{
		            	 outputStream2 = new PrintWriter(new FileOutputStream(current+"\\queryresult\\queryresult2.txt"));
		             }
		             catch(FileNotFoundException e){
		            	 System.out.println("Error to find file queryresult2.txt");
		            	 System.exit(0);
		             }
				    //go through all triple in sparql repository
		            while (result2.hasNext()) {  // iterate over the result
						BindingSet bindingSet = result2.next();
						Value valueOfS = bindingSet.getValue("s");
						Value valueOfN = bindingSet.getValue("n");
						
						System.out.println(valueOfS + " " + valueOfN);

			            outputStream2.println(valueOfS + " " + valueOfN);
			             
					
		           }
		           outputStream2.close();
			  }
			  finally {
			      result2.close();
			  }
			  
    }
	private static void verifyArgs() {
        
        if (!new File("input").exists())
            usageError("file " + "input" + " doesn't exist");
        File outdir = new File("output");
        if (!outdir.exists() && !outdir.mkdirs())
            usageError("couldn't create output dir");
    
}

private static void usageError(String s) {
    System.err.println(s);
    System.err.println("Usage: java " + (new Object() { }.getClass().getEnclosingClass()).getName() + " input_dir output_dir");
    System.exit(-1);
}
	
}


