import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IActionKey, ActionKey } from '../action-key.model';

import { ActionKeyService } from './action-key.service';

describe('ActionKey Service', () => {
  let service: ActionKeyService;
  let httpMock: HttpTestingController;
  let elemDefault: IActionKey;
  let expectedResult: IActionKey | IActionKey[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ActionKeyService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      action: 'AAAAAAA',
      keys: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ActionKey', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ActionKey()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ActionKey', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          action: 'BBBBBB',
          keys: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ActionKey', () => {
      const patchObject = Object.assign({}, new ActionKey());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ActionKey', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          action: 'BBBBBB',
          keys: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ActionKey', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addActionKeyToCollectionIfMissing', () => {
      it('should add a ActionKey to an empty array', () => {
        const actionKey: IActionKey = { id: 123 };
        expectedResult = service.addActionKeyToCollectionIfMissing([], actionKey);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(actionKey);
      });

      it('should not add a ActionKey to an array that contains it', () => {
        const actionKey: IActionKey = { id: 123 };
        const actionKeyCollection: IActionKey[] = [
          {
            ...actionKey,
          },
          { id: 456 },
        ];
        expectedResult = service.addActionKeyToCollectionIfMissing(actionKeyCollection, actionKey);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ActionKey to an array that doesn't contain it", () => {
        const actionKey: IActionKey = { id: 123 };
        const actionKeyCollection: IActionKey[] = [{ id: 456 }];
        expectedResult = service.addActionKeyToCollectionIfMissing(actionKeyCollection, actionKey);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(actionKey);
      });

      it('should add only unique ActionKey to an array', () => {
        const actionKeyArray: IActionKey[] = [{ id: 123 }, { id: 456 }, { id: 54063 }];
        const actionKeyCollection: IActionKey[] = [{ id: 123 }];
        expectedResult = service.addActionKeyToCollectionIfMissing(actionKeyCollection, ...actionKeyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const actionKey: IActionKey = { id: 123 };
        const actionKey2: IActionKey = { id: 456 };
        expectedResult = service.addActionKeyToCollectionIfMissing([], actionKey, actionKey2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(actionKey);
        expect(expectedResult).toContain(actionKey2);
      });

      it('should accept null and undefined values', () => {
        const actionKey: IActionKey = { id: 123 };
        expectedResult = service.addActionKeyToCollectionIfMissing([], null, actionKey, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(actionKey);
      });

      it('should return initial array if no ActionKey is added', () => {
        const actionKeyCollection: IActionKey[] = [{ id: 123 }];
        expectedResult = service.addActionKeyToCollectionIfMissing(actionKeyCollection, undefined, null);
        expect(expectedResult).toEqual(actionKeyCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
