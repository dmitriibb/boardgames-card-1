import { TestBed } from '@angular/core/testing';

import { MessageExchangeService } from './message-exchange.service';

describe('MessageExchangeService', () => {
  let service: MessageExchangeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MessageExchangeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
