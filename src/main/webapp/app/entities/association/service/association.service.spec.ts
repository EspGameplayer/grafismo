import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAssociation, Association } from '../association.model';

import { AssociationService } from './association.service';

describe('Association Service', () => {
  let service: AssociationService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssociation;
  let expectedResult: IAssociation | IAssociation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssociationService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a Association', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Association()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Association', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Association', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new Association()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Association', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a Association', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssociationToCollectionIfMissing', () => {
      it('should add a Association to an empty array', () => {
        const association: IAssociation = { id: 123 };
        expectedResult = service.addAssociationToCollectionIfMissing([], association);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(association);
      });

      it('should not add a Association to an array that contains it', () => {
        const association: IAssociation = { id: 123 };
        const associationCollection: IAssociation[] = [
          {
            ...association,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssociationToCollectionIfMissing(associationCollection, association);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Association to an array that doesn't contain it", () => {
        const association: IAssociation = { id: 123 };
        const associationCollection: IAssociation[] = [{ id: 456 }];
        expectedResult = service.addAssociationToCollectionIfMissing(associationCollection, association);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(association);
      });

      it('should add only unique Association to an array', () => {
        const associationArray: IAssociation[] = [{ id: 123 }, { id: 456 }, { id: 55377 }];
        const associationCollection: IAssociation[] = [{ id: 123 }];
        expectedResult = service.addAssociationToCollectionIfMissing(associationCollection, ...associationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const association: IAssociation = { id: 123 };
        const association2: IAssociation = { id: 456 };
        expectedResult = service.addAssociationToCollectionIfMissing([], association, association2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(association);
        expect(expectedResult).toContain(association2);
      });

      it('should accept null and undefined values', () => {
        const association: IAssociation = { id: 123 };
        expectedResult = service.addAssociationToCollectionIfMissing([], null, association, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(association);
      });

      it('should return initial array if no Association is added', () => {
        const associationCollection: IAssociation[] = [{ id: 123 }];
        expectedResult = service.addAssociationToCollectionIfMissing(associationCollection, undefined, null);
        expect(expectedResult).toEqual(associationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
