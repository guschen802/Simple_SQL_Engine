CREATE TABLE LINEITEM (
        orderkey       INT,
        partkey        INT,
        suppkey        INT,
        linenumber     INT,
        quantity       DECIMAL,
        extendedprice  DECIMAL,
        discount       DECIMAL,
        tax            DECIMAL,
        returnflag     CHAR(1),
        linestatus     CHAR(1),
        shipdate       DATE,
        commitdate     DATE,
        receiptdate    DATE,
        shipinstruct   CHAR(25),
        shipmode       CHAR(10),
        comment        VARCHAR(44)
    );
CREATE TABLE ORDERS (
        orderkey       INT,
        custkey        INT,
        orderstatus    CHAR(1),
        totalprice     DECIMAL,
        orderdate      DATE,
        orderpriority  CHAR(15),
        clerk          CHAR(15),
        shippriority   INT,
        comment        VARCHAR(79)
    );
CREATE TABLE CUSTOMER (
        custkey      INT,
        name         VARCHAR(25),
        address      VARCHAR(40),
        nationkey    INT,
        phone        CHAR(15),
        acctbal      DECIMAL,
        mktsegment   CHAR(10),
        comment      VARCHAR(117)
    );
    
SELECT 
    lineitem.orderkey, 
    sum(lineitem.extendedprice * (1 - lineitem.discount)) AS revenue, 
    orders.orderdate, 
    orders.shippriority 

FROM 
    customer, orders, lineitem 

WHERE 
    customer.mktsegment = 'MACHINERY' 
    AND customer.custkey = orders.custkey 
    AND lineitem.orderkey = orders.orderkey 
    AND orders.orderdate < DATE('1995-03-06') 
    AND lineitem.shipdate > DATE('1995-03-06') 

GROUP BY 
    lineitem.orderkey, orders.orderdate, orders.shippriority 
    
ORDER BY 
    revenue DESC, orderdate 
    
LIMIT 10;