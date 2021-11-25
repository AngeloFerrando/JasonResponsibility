// Agent robot in project DomesticRobot.mas2j

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */
	
+!start : not delivering <- 
	!at(robot, door);
	!at(robot, fridge);
	!at(robot, owner);
	!start.
+!start : true <- +ready_to_deliver.

+!at(robot,P) : at(robot,P) <- .print(P, " reached").
+!at(robot,P) : not at(robot,P)
  <- move_towards(2, P);
     !at(robot,P).
  
// when the supermarket makes a delivery, try the 'has' goal again
+delivered(beer,_Qtd,_OrderId)
  :  at(robot, door)
  <- 
  	!at(robot, fridge);
	.send(robotBeer, tell, available(beer,fridge));
	-delivering;
	-ready_to_deliver;
	!!start.
+delivered(beer,_Qtd,_OrderId)
  : at(robot, P)
  <-
  .wait(1000);
  -+delivered(beer, _, _).

// when the fridge is opened, the beer stock is perceived
// and thus the available belief is updated
+stock(beer,0)
   : true
   <- 
   	  +delivering;
	  .wait(ready_to_deliver);
      -stock(_, _);
   	  .print("No more beer, I need to order them.");
   	  .send(supermarket, achieve, order(beer,5));
      !at(robot,door).
+stock(beer,N)
   : true <- -stock(_, _); .print("Stock perceived: ", stock(beer, N)).

