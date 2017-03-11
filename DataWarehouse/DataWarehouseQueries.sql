--number of customers per country
SELECT country, count(customer_id) from customers group by (country);
--SELECT  ship_country, count(customer_id) from orders group by (ship_country);
--number of orders per country (country and city) and in total. 
SELECT ship_country, ship_city, count(customer_id) as nborders from orders group by rollup(ship_country,ship_city);
--number of orders and quantity of items shipped (according to order details). for each pair of Customer
--country and supplier country. Order result by customer country first, then supplier country. 
SELECT customers.country as c_country,orders.ship_country as s_country,
sum(order_details.quantity)as quantity,count(distinct orders.order_id) as nborder
FROM orders, order_details, customers WHERE
orders.order_id = order_details.order_id AND
orders.customer_id = customers.customer_id 
group by (c_country,s_country);
--Number of orders of orders and quantity for all the cube levels when we only consider
--Customer and Supplier geography at the top and country levels only
SELECT customers.country as c_country,orders.ship_country as s_country,
sum(order_details.quantity)as quantity,count(distinct orders.order_id) as nborder
FROM orders, order_details, customers WHERE
orders.order_id = order_details.order_id AND
orders.customer_id = customers.customer_id 
group by cube(c_country,s_country);
--Total price (quantity*unitprice) of orders with french suppliers for each country
--
SELECT orders.ship_country as s_country,
orders.ship_region as s_region,
orders.ship_city as s_city,
sum(order_details.quantity*order_details.unit_price)as total_price
FROM orders, order_details WHERE
orders.order_id = order_details.order_id
group by s_country, rollup(s_region,s_city);
--Modify query from question 2 so that the string 'whole country´is
--displayed insteadof NULL on every row that aggregates all cities of 
--a single country.
SELECT ship_country, case when ship_city is null then 'WHOLE COUNTRY' 
else ship_city end, count(order_id) as nborders from orders 
group by rollup(ship_country,ship_city);

-------------------------
--Windowing aggregates
-------------------------
--Number of orders per country and city on one column, together with 
--toal number of orders over the cities in this countries on other 
--columns. 
SELECT ship_country, ship_city, count(order_id) as nborders,
sum(count(order_id)) 
OVER (PARTITION BY ship_country) as nbordcty,
max(count(order_id)) OVER (PARTITION BY ship_country)
FROM orders group by ship_country,ship_city
order by ship_country, ship_city

--Cities ranked within countries by number of orders displaying number
--of orders and rank. There should not be any gaps in the ranks even 
--presence of ties.

SELECT ship_country, ship_city,count(order_id) as nbordes, rank() 
OVER (PARTITION BY ship_country ORDER BY count(order_id)) FROM ORDERS group by ship_country, 
ship_city; 

--Add to previous query the percentage of the total number of orders
--reached by each city within a country.
SELECT ship_country, ship_city,count(order_id) as nbordes, rank()
OVER (PARTITION BY ship_country ORDER BY count(order_id)),
count(order_id)/sum(count(order_id))
OVER (PARTITION BY ship_country) FROM ORDERS group by ship_country, 
ship_city

--Total price for each order, filtering out all the orders whose price
--does not exceed that of preceding (next high OrderID) order by more 
--than 10% (you may nest queries)
SELECT * FROM
(SELECT distinct(order_id), sum(unit_price*quantity) as price
, lag(sum(unit_price*quantity),1) OVER (ORDER BY 
order_id) as previous_price
from order_details
group by order_id)as temp
WHERE price<previous_price*1.1
ORDER BY order_id;

--Product sold in highest quantity per country and year with the quantity.
--Propose one answer using windowing functions and another that does not. 
SELECT ship_country , order_date
from orders group by ship_country,order_date ;


-----------------------------------------------------
--CLASS SOLUTIONS
-----------------------------------------------------
/*
1.4.1 SUM(COUNT(*)) OVER (PARTITION BY S_COUNTRY) MAX() GROUP BY S_COUNTRY,CITY
1.4.2 For the percentage
DENSE_RANK() OVER (PARTITION BY SHIP_COUNTRY ORDER BY COUNT(*) GROUP BY 
SHIP_COUNTRY, SHIP_CITY
1.4.3 Look for ratio_to_report in oracle or do it yourself 
COUNT(*) DENSE_RANK() OVER (PARTITION BY S_COUNTRY ORDER BY COUNT(*))
RATIO_TO_REPORT(COUNT(*) OVER (PARTITION BY S_COUNTRY)
1.4.4 Filter out a line if it is > 110% of previous line order by
order_id
SELECT .... (SELECT SUM(...) price, LAG(SUM) OVER... PRICE- PREVIOUS) TEMP
where PRICE<PRICE_PREVIOUS
1.4.5 SELECT Y , p_name, quantity FROM TEMP
NATURAL JOIN (SELECT Y, MAX(QUANTITY) QTY from temp group by) 
*/

---------------------------------------------
--HIERARCHICAL QUERIES
--------------------------------------------

WITH RECURSIVE t(n) AS (
    VALUES (1)
  UNION ALL
    SELECT n+1 FROM t WHERE n < 60
)
SELECT n FROM t;

--ORACLE

WITH d(n) AS(
select 1 from dual
union all
select n+1 FROM d 
WHERE n < 60)
select n from d;

WHERE d(da) AS(
select TO_CHAR(SYSDATE, 'MM-YY') FROM DUAL
union all
select add_months(da,1) FROM d
WHERE 


