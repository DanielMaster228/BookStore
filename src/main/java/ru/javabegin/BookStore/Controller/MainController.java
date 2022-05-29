package ru.javabegin.BookStore.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.BookStore.JsonParser;
import ru.javabegin.BookStore.entity.Deal;
import ru.javabegin.BookStore.entity.MainServer;
import ru.javabegin.BookStore.entity.account;
import ru.javabegin.BookStore.entity.books;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {

    private final MainServer mainServer;
    private final JsonParser jsonParser = new JsonParser();

    private MainController(){
        mainServer = jsonParser.FromJson();
    }

    @GetMapping("/")
    public ResponseEntity<String> main(){
        return ResponseEntity.ok("Книжный интернет-магазин");
    }

    @GetMapping("/market")
    public ResponseEntity<List<books>> books(){
        for(books i : mainServer.getBooks()) {
            if(i.getAmount() == 0) {
                mainServer.getBooks().remove(i);
            }
        }
        return ResponseEntity.ok(mainServer.getBooks());
    }

    @GetMapping("/account")
    public ResponseEntity<account> account(){
        return ResponseEntity.ok(mainServer.getAccount());
    }

    @PostMapping(path = "/market/deal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<account> MessageMarketDeal(@RequestBody Deal newDeal){
        if(mainServer.getAccount().getBooks() == null && mainServer.getAccount().getMoney() >= mainServer.getBooks().get(newDeal.getId()).getPrice() * newDeal.getAmount() && mainServer.getBooks().get(newDeal.getId()).getAmount() >= newDeal.getAmount()){
            mainServer.getAccount().setBooks(new ArrayList<books>() {{add(mainServer.getBooks().get(newDeal.getId())); get(0).setAmount(newDeal.getAmount());}});
            mainServer.getAccount().setMoney(mainServer.getAccount().getMoney() - mainServer.getBooks().get(newDeal.getId()).getPrice() * newDeal.getAmount());
            mainServer.getBooks().get(newDeal.getId()).setAmount(mainServer.getBooks().get(newDeal.getId()).getAmount() - newDeal.getAmount());
            jsonParser.ToJson(mainServer);
            return new ResponseEntity<>(mainServer.getAccount(), HttpStatus.OK);
        }
        if(mainServer.getAccount().getMoney() >= mainServer.getBooks().get(newDeal.getId()).getPrice() * newDeal.getAmount() && mainServer.getBooks().get(newDeal.getId()).getAmount() >= newDeal.getAmount()){
            if(mainServer.getAccount().checkId(newDeal.getId())){
                books NewBook = new books();
                NewBook.setId(mainServer.GetBookById(newDeal.getId()).getId());
                NewBook.setAuthor(mainServer.GetBookById(newDeal.getId()).getAuthor());
                NewBook.setAmount(newDeal.getAmount());
                NewBook.setName(mainServer.GetBookById(newDeal.getId()).getName());
                NewBook.setPrice(mainServer.GetBookById(newDeal.getId()).getPrice());
                mainServer.getAccount().getBooks().add(NewBook);
            }
            else {
                mainServer.getAccount().GetBookById(newDeal.getId()).setAmount(mainServer.getAccount().GetBookById(newDeal.getId()).getAmount() + newDeal.getAmount());
            }
            mainServer.getAccount().setMoney(mainServer.getAccount().getMoney() - mainServer.getBooks().get(newDeal.getId()).getPrice() * newDeal.getAmount());
            mainServer.getBooks().get(newDeal.getId()).setAmount(mainServer.getBooks().get(newDeal.getId()).getAmount() - newDeal.getAmount());
            jsonParser.ToJson(mainServer);
            return new ResponseEntity<>(mainServer.getAccount(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
