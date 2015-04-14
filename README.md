# Web-retriever-rdf-triple-mapper
you will need to write a program to extract name entities from
online news and load them into a triple store.
1. Choose 2 special news articles online you are interested in (e.g., from Google
news). “Special” means to avoid potential overlap with other students.
2. Your program should use the REST API from OpenCalais:
(http://www.opencalais.com) to get the name entities in RDF and load them
into a triple store.
3. Use SPARQL queries to extract the “Person”, “Organization” and “City” from
the triple store. You can type the queries in the OpenRDF workbench or
implement them in your code. Calais Viewer (http://viewer.opencalais.com) is
a good for you to verify your results. All the extract entities are shown on the
left side of the page.
4. To evaluate the recognition results, you need to manually label all the “Person”,
“Organization” and “City” in the two articles. You may use your manually
labeled data as ground truth to calculate the precision, recall and F1 score
(http://en.wikipedia.org/wiki/Precision_and_recall) of the article. You need to
explain in which cases the recognition performs poorly and well, and why (the
possible causes).
