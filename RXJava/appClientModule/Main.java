import io.reactivex.rxjava3.core.Observable;

/* 
 * Reactive Programming이란
 * 
 * 우리는 주로 알고리즘 문제와 같이 절차를 명시하여 순서대로 실행되는 Imperative Programming(명령형 프로그래밍)을 한다. 
 * 반면 Reactive Programming이란 데이터의 흐름을 먼저 정의하고 데이터가 변경되었을 때 연관된 작업이 실행된다. 
 * 즉 프로그래머가 어떠한 기능을 직접 정해서 실행하는 것이 아닌, 시스템에 이벤트가 발생했을 때 알아서 처리되는 것이다.
 * 
 * 
 * Pull 방식은 데이터를 사용하는 곳(Consumer)에서 데이터를 직접 가져와서 사용한다면,
 * Push 방식은 데이터의 변화가 발생한 곳에서 새로운 데이터를 Consumer에게 전달한다.
 * 
 * 
 * Observable & Operator(연산자)
 *  
 * Observable : ReactiveX의 핵심 요소이자 데이터 흐름에 맞게 Consumer에게 알림을 보내는 Class이다.
 * just() : 가장 간단한 Observable 생성 방식이다. (생성 연산자라고도 한다)
 * map() : RxJava의 연산자이다. 데이터를 원하는 형태로 바꿀 수 있다.
 * subscribe() : Observable은 구독(subscribe)을 해야 데이터가 발행된다. 따라서 Observable을 구독하여 데이터를 발행 후, 수신한 데이터를 원하는 방식으로 사용(System.out::println)한다
 * 
 * 
 * Observable이란
 * 
 * RxJava의 가장 핵심적인 요소는 Observable이다. Observable은 데이터 흐름에 맞게 알림을 보내 Observer가 데이터를 사용할 수 있도록 한다.
 * 즉, Observable을 이용해 데이터를 회수하고 변환하는 메커니즘을 정의하고, Observer는 이를 구독해 데이터가 준비되면 이에 반응한다.
 * 
 * Observable은 Collections(List, ArrayList, …)를 사용할 때와 같은 방식으로 비동기 이벤트 스트림을 처리할 수 있다. 
 * 다만 Collections의 Iterable이 Pull 방식이라면, Observable은 Iterable의 Push 버전이다.
 * Iterable은 Consumer(데이터를 소비하는 곳)가 값을 Pull한 후 값이 도착할 때까지 기다리며 Thread를 차단한다면, Observable은 Thread를 차단하지 않고 값이 사용가능하면 Consumer에게 값을 Push한다.
 * 
 * 정리 
 * 1) Observable이 데이터 스트림을 처리하고, 완료되면 데이터를 발행(emit)한다.
 * 2) 데이터를 발행할 때마다 구독하고 있는 모든 Observer가 알림을 받는다.
 * 3) Observer는 수신한 데이터를 가지고 어떠한 일을 한다.
 * 
 * 종류
 * RxJava2와 RxJava3에는 데이터 소스를 나타내는 5가지의 기본 클래스가 있다.
 * 
 * Observable : 가장 기본적인 형태, 0개~N개의 데이터 발행, BackPressure 없음
 * Single : 단 1개의 데이터, 혹은 오류 발행
 * Completable : 성공 혹은 실패했다는 결과만 발행
 * Maybe : 0개 또는 1개 완료, 오류
 * Flowable : 0개~N개의 데이터 발행, BackPressure 존재
 * 
 * Observable로 변환
 * fromXXX()
 * 여러 데이터를 다뤄야 하는 경우 사용한다. 특정 타입의 데이터를 Observable로 바꿔주는 메소드이다. 
 * 
 * Create	onNext, onError, onComplete를 일일이 명시하며 Observable 생성
 * Defer	Observer가 구독할 때까지 기다렸다가 구독하면 그 때 Observable 생성
 * Empty/Never/Throw	아이템을 0개 방출한 후 종료/종료하지않음/에러발생
 * From	다른 객체를 Observable로 변환
 * Interval	시간 간격을 두고 데이터를 방출하는 Observable 생성
 * Just	parameter로 전달한 아이템을 그대로 발행하는 Observable 생성
 * Range	특정 범위 내 Integer 형태의 아이템 발행하는 Observable 생성
 * Repeat	아이템을 지정한 횟수만큼, 혹은 무한히 반복하여 발행
 * Start	연산 후 특정 값을 반환, 함수처럼 작용함
 * Timer	지정한 시간 delay 이후 아이템 발행
 * 
 * 스케줄러란
 * RxJava의 스케줄러는 RxJava의 코드가 어느 스레드에서 실행될 것인지 지정하는 역할을 한다. RxJava만 사용한다고 비동기 처리가 되는 것이 아니라, 
 * 스케줄러를 통해 스레드를 분리해주어야 비동기 작업이 가능한 것이다. 스케줄러의 지정은 RxJava의 subscribeOn 과 observeOn 연산자를 통해 가능하다.
 * 
 * subscribeOn은 Observable이 데이터 흐름을 발생시키고 연산하는 스레드를 지정할 수 있고
 * observeOn은 Observable이 Observer에게 알림을 보내는 스레드를 지정할 수 있다.
 * 
 * 
 */
public class Main {
	public static void main(String[] args) {
		test2();
	}
	
	static void test1(){
		// Observable 생성
        Observable<String> observable = Observable.create(emitter -> {
            emitter.onNext("Hello");
            emitter.onNext("RxJava");
            emitter.onNext("3");
            emitter.onComplete();
            // onComplete 이후 데이터는 발행되지 않는다.
            emitter.onNext("4");
        });

        // Observer (구독자) 생성
        observable.subscribe(
            item -> System.out.println("Received: " + item), // onNext 처리기
            throwable -> System.err.println("Error: " + throwable), // onError 처리기
            () -> System.out.println("Completed") // onComplete 처리기
        );
	}

	static void test2() {
		Integer [] array = {1, 2, 3, 4, 5};
		Observable.fromArray(array)
		        .subscribe(System.out::println);
	}
	
}