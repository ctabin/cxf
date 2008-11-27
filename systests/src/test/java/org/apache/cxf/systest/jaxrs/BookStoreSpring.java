/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.cxf.systest.jaxrs;


import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

@Path("/bookstore")
@Produces("application/json")
public class BookStoreSpring {

    private Map<Long, Book> books = new HashMap<Long, Book>();
    private Long mainId = 123L;
    @Context
    private UriInfo ui;    
    
    public BookStoreSpring() {
        init();
        System.out.println("----books: " + books.size());
    }
    
    @GET
    @Path("/books/list/{id}")
    public Books getBookAsJsonList(@PathParam("id") Long id) {
        return new Books(books.get(id));
    }
    
    @GET
    @Path("/books/{id}")
    public Book getBookById(@PathParam("id") Long id) {
        return books.get(id);
    }
    
    @GET
    @Path("/bookinfo")
    public Book getBookByUriInfo() throws Exception {
        MultivaluedMap<String, String> params = ui.getQueryParameters();
        String id = params.getFirst("param1") + params.getFirst("param2");
        return books.get(Long.valueOf(id));
    }
    
    @GET
    @Path("/booksquery")
    public Book getBookByQuery(@QueryParam("id") String id) {
        
        String[] values = id.split("\\+");
        StringBuilder b = new StringBuilder();
        b.append(values[0]).append(values[1]);        
        return books.get(Long.valueOf(b.toString()));
    }
     
    @GET
    @Path("id={id}")
    public Book getBookByEncodedId(@PathParam("id") String id) {
        String[] values = id.split("\\+");
        StringBuilder b = new StringBuilder();
        b.append(values[0]).append(values[1]);        
        return books.get(Long.valueOf(b.toString()));
    }
    
    
    @GET
    public Book getDefaultBook() {
        return books.get(mainId);
    }  

    @POST
    @Path("books/convert")
    @Consumes({"application/xml", "application/json" })
    @Produces("application/xml")
    public Book convertBook(Book2 book) {
        // how to have Book2 populated ?
        Book b = new Book();
        b.setId(book.getId());
        b.setName(book.getName());
        return b;
    }
    
    
    
    final void init() {
        Book book = new Book();
        book.setId(mainId);
        book.setName("CXF in Action");
        books.put(book.getId(), book);
    }
    
}


