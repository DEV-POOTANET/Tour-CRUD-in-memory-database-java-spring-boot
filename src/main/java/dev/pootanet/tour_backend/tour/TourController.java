package dev.pootanet.tour_backend.tour;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
@RequestMapping("/tours")
public class TourController {
    private final Map<Integer,Tour>tourInMemDb;
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(1);

    public TourController(){
        tourInMemDb = new HashMap<>();
    }

    // ---------
    // crud tour
    // ---------

    @GetMapping   
    public List<Tour> getTours() {
        return tourInMemDb.entrySet().stream()     // เข้าถึงทุกคู่ key-value ใน Map และสร้างเป็น stream
                .map(Map.Entry::getValue)          // ใช้ map เพื่อดึงเฉพาะ value ของแต่ละ entry (ค่า Tour)
                .toList();                         // แปลง stream ให้เป็น List ของ Tour
    }

    @GetMapping("/{id}")    
    public Tour getTourById(@PathVariable("id") int id) {
        return Optional.ofNullable(tourInMemDb.get(id))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tour not found"));
    }
    
    @PostMapping   
    @ResponseStatus(HttpStatus.CREATED)
    public Tour creatTour(@RequestBody Tour tour){
        var newTour =new Tour(
            ATOMIC_INTEGER.getAndIncrement(),
            tour.title(),
            tour.maxPeople()
        );
        var id = newTour.id();
        tourInMemDb.put(id,newTour);
        return tourInMemDb.get(id);
    }

    @PutMapping("/{id}")
    public Tour putMethodName(@PathVariable("id") int id, @RequestBody Tour tour) {
        var updateTour =new Tour(
            id,
            tour.title(),
            tour.maxPeople()
        );
        tourInMemDb.put(id,updateTour);
        return tourInMemDb.get(id);
    }

    @DeleteMapping("/{id}")
    public String deleteTour(@PathVariable("id") int id){
        if(!tourInMemDb.containsKey(id)){
            return "Failed to delete";
        }
        tourInMemDb.remove(id);
        return "Success to delete" + id;
    }

}
